package com.bytebyte6.common

import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * 用于转场动画结束时清理资源
 */
interface ImageClearHelper {
    val images: MutableSet<ImageView>
    fun clear()
}

class GlideClearHelper : ImageClearHelper {
    override val images: MutableSet<ImageView> = mutableSetOf()
    override fun clear() {
        images.forEach {
            Glide.with(it.context).clear(it)
        }
        images.clear()
    }
}