package com.bytebyte6.view

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class RtmpGlide : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {

    }
}

fun ImageView.load(url: String) {
    GlideApp.with(context)
        .load(url)
        .placeholder(R.drawable.landscape)
        .error(R.drawable.image_not_found)
        .into(this)
}