package com.bytebyte6.usecase

import androidx.annotation.Keep
import com.bytebyte6.data.entity.Tv
import com.google.android.exoplayer2.offline.Download

@Keep
data class TvAndDownload(
    val tv: Tv,
    val download: Download
)