package com.rguerra.data.network.entities.post

import com.google.gson.annotations.SerializedName

data class NewPostEntity(
        @SerializedName("title")
        val title: String,
        @SerializedName("body")
        val body: String,
        @SerializedName("userId")
        val userId: Int
)