package com.rguerra.data.repository

import com.rguerra.data.network.api.Api
import com.rguerra.domain.models.Album
import com.rguerra.domain.models.Post
import com.rguerra.domain.models.User
import com.rguerra.domain.models.request.ByPostIdRequest
import com.rguerra.domain.models.request.ByUserIdRequest
import com.rguerra.domain.models.request.NewPostRequest
import com.rguerra.domain.repositories.RepositoryContractor
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class Repository(
        private val api: Api,
        private val storage: Storage
) : RepositoryContractor {

    /**
     * Get all the data needed to start the app and store it temporarily
     */
    override fun seedUsers(): Completable {
        return fetchUsers()
                .doOnSuccess { storage.users = it }
                .flatMapObservable { Observable.fromIterable(it) }
                .flatMapSingle { user ->
                    val userId = user.id
                    fetchPosts(ByUserIdRequest(userId = userId))
                            .doOnSuccess { storage.posts[userId] = it }
                            .flatMap { fetchAlbums(ByUserIdRequest(userId = userId)) }
                            .doOnSuccess { storage.albums[userId] = it }
                }.ignoreElements()
    }

    /**
     * If there is cache, get Users from the cache
     **/
    override fun getUsers(): Single<List<User>> =
            if (storage.users != null) {
                Single.just(storage.users)
            } else {
                fetchUsers()
            }

    override fun getPosts(request: ByUserIdRequest): Single<List<Post>> =
            if (storage.posts.isNotEmpty()) {
                Single.just(storage.posts[request.userId])
            } else {
                fetchPosts(request)
            }

    override fun getAlbums(request: ByUserIdRequest): Single<List<Album>> =
            if (storage.albums.isNotEmpty()) {
                Single.just(storage.albums[request.userId])
            } else {
                fetchAlbums(request)
            }

    override fun addNewPost(request: NewPostRequest): Completable =
            api.addNewPost(request)
                    .doOnSuccess { addFakePost(request) }
                    .ignoreElement()

    /**
     * Once api is fake, it returns 201 = created, but it doesn't add it to the getPosts,
     * therefore fakePost will simulate that it was added
     */
    private fun addFakePost(request: NewPostRequest) {
        val userId = request.userId
        storage.posts[userId]?.let {
            val fakePost = Post(id = it.last().id + 1, title = request.title, body = request.body)
            val updatedList = it.toMutableList()
            updatedList.add(fakePost)

            storage.posts.put(userId, updatedList)
        }
    }

    override fun deletePost(actionByUserIdRequest: ByPostIdRequest): Completable = api.deletePost(actionByUserIdRequest).ignoreElement()

    private fun fetchUsers() =
            api.getUsers()
                    .map { list ->
                        list.map { it.toDomain() }
                    }

    private fun fetchPosts(request: ByUserIdRequest): Single<List<Post>> =
            api.gePosts(request)
                    .map { list ->
                        list.map { it.toDomain() }
                    }

    private fun fetchAlbums(request: ByUserIdRequest): Single<List<Album>> =
            api.getAlbums(request)
                    .map { list ->
                        list.map { it.toDomain() }
                    }
}