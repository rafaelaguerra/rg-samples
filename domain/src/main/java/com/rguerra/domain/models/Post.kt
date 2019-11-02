package com.rguerra.domain.models

data class Post(
        val id : Int,
        val title: String,
        val body: String
) : BaseModel()