package com.rguerra.domain.repositories

import com.rguerra.domain.models.Album
import com.rguerra.domain.models.Post
import com.rguerra.domain.models.User
import com.rguerra.domain.models.request.ByPostIdRequest
import com.rguerra.domain.models.request.ByUserIdRequest
import com.rguerra.domain.models.request.NewPostRequest
import io.reactivex.Completable
import io.reactivex.Single

interface RepositoryContractor {
    fun getUsers(): Single<List<User>>

    fun seedUsers(): Completable

    fun getAlbums(request: ByUserIdRequest): Single<List<Album>>

    fun getPosts(request: ByUserIdRequest): Single<List<Post>>

    fun deletePost(actionByUserIdRequest: ByPostIdRequest): Completable

    fun addNewPost(byPostIdRequest: NewPostRequest): Completable
}