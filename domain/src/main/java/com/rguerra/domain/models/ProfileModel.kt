package com.rguerra.domain.models

data class ProfileModel(val id: Int,
                        val name: String,
                        val numberOfPosts: Int,
                        val numberOfAlbums: Int,
                        val company: String)