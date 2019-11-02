package com.rguerra.data.network.api

import com.rguerra.data.network.entities.BaseEntity
import com.rguerra.data.network.entities.album.AlbumEntity
import com.rguerra.data.network.entities.post.NewPostEntity
import com.rguerra.data.network.entities.post.PostEntity
import com.rguerra.data.network.entities.user.UserEntity
import com.rguerra.data.network.service.Service
import com.rguerra.domain.models.request.ByPostIdRequest
import com.rguerra.domain.models.request.ByUserIdRequest
import com.rguerra.domain.models.request.NewPostRequest
import io.reactivex.Single

class Api(private val service: Service) {

    fun getUsers(): Single<List<UserEntity>> = service.getUsers()

    fun gePosts(request: ByUserIdRequest): Single<List<PostEntity>> = service.getPosts(request.userId)

    fun getAlbums(request: ByUserIdRequest): Single<List<AlbumEntity>> = service.getAlbums(request.userId)

    fun deletePost(request: ByPostIdRequest): Single<BaseEntity> = service.deletePost(request.postId)

    fun addNewPost(request: NewPostRequest): Single<BaseEntity> = service.addNewPost(NewPostEntity(request.title, body = request.body, userId = request.userId))

}