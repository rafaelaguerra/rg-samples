package com.rguerra.presentation.models

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class ProfileUi(
        val id: Int,
        val name: String,
        var selected: Boolean = false,
        val numberPosts: String,
        val numberAlbums: String,
        val company: String
) : Parcelable

