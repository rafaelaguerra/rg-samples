package com.rguerra.data.network.entities

import com.google.gson.annotations.SerializedName

data class BaseEntity(
        @SerializedName("code")
        val code: Int,
        @SerializedName("message")
        val message: String
)