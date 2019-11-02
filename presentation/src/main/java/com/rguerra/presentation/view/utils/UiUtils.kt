package com.rguerra.profiles.view.utils

import android.view.View

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.toggleVisibility(visible: Boolean) {
    visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}