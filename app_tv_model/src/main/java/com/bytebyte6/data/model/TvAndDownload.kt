package com.bytebyte6.data.model

import androidx.annotation.Keep
import com.bytebyte6.data.entity.Tv
import com.google.android.exoplayer2.offline.Download

@Keep
data class TvAndDownload(
    val tv: Tv,
    val download: Download
):Card{
    override val title: String
        get() = tv.name
    override val body: String
        get() = tv.category
    override val outline: Boolean
        get() = false
    override val color: Int
        get() = 0
    override val radius: Int
        get() = 0
    override val transitionName: String
        get() = tv.transitionName
}