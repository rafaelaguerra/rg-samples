package com.rguerra.data.network.entities.post

import com.google.gson.annotations.SerializedName
import com.rguerra.domain.models.Post

data class PostEntity(
        @SerializedName("id")
        val id: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("body")
        val body: String
) {
    fun toDomain() = Post(
            id = id,
            title = title,
            body = body
    )
}