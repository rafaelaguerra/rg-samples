package com.rguerra.domain.models.request

data class NewPostRequest(val title: String, val body: String, val userId: Int)