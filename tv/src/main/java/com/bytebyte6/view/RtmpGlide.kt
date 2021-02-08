package com.bytebyte6.view

import android.content.Context
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class RtmpGlide : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder/*.setLogLevel(Log.ERROR)*/
            .setDefaultRequestOptions(
                GlideOptions()
                    .dontAnimate()
                    .dontTransform()
                    .placeholder(R.drawable.landscape)
                    .error(R.drawable.image_not_found2)
            )
    }
}

fun ImageView.load(url: String) {
    GlideApp.with(context)
        .load(url)
        .into(this)
}