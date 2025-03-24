package com.example.myapplication.util

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun ImageView.loadImage(url: String?) {
    if (url != null) {
        Glide.with(this.context)
            .load(url)
            .apply(RequestOptions().centerCrop())
            .into(this)
    }
}