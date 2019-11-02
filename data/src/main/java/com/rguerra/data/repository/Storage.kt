package com.rguerra.data.repository

import com.rguerra.domain.models.Album
import com.rguerra.domain.models.Post
import com.rguerra.domain.models.User

class Storage {
    var users: List<User>? = null
    //<userId, Post>
    val posts: MutableMap<Int, List<Post>> = mutableMapOf()
    //<userId, Album>
    val albums: MutableMap<Int, List<Album>> = mutableMapOf()
}