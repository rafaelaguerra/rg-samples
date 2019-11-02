package com.rguerra.data.network.entities.album

import com.google.gson.annotations.SerializedName
import com.rguerra.domain.models.Album

data class AlbumEntity(
        @SerializedName("id")
        val id: Int,
        @SerializedName("title")
        val title: String
) {
    fun toDomain() = Album(
            title = title)
}