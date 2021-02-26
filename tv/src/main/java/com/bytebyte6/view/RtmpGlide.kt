package com.bytebyte6.view

import android.content.Context
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class RtmpGlide : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) = Unit
}

fun ImageView.load(url: String) {
    GlideApp.with(context)
        .load(url)
        .placeholder(R.drawable.landscape)
        .into(this)
}