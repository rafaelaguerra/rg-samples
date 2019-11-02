package com.rguerra.data.network.service

import com.rguerra.data.network.entities.BaseEntity
import com.rguerra.data.network.entities.album.AlbumEntity
import com.rguerra.data.network.entities.post.NewPostEntity
import com.rguerra.data.network.entities.post.PostEntity
import com.rguerra.data.network.entities.user.UserEntity
import io.reactivex.Single
import retrofit2.http.*

interface Service {

    @GET("users")
    fun getUsers(): Single<List<UserEntity>>

    @GET("albums")
    fun getAlbums(@Query("userId") userId: Int): Single<List<AlbumEntity>>

    @GET("posts")
    fun getPosts(@Query("userId") userId: Int): Single<List<PostEntity>>

    @POST("posts")
    fun addNewPost(@Body entity: NewPostEntity): Single<BaseEntity>

    @DELETE("posts/{postId}")
    fun deletePost(@Path("postId") postId: Int): Single<BaseEntity>
}
