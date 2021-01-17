package com.bytebyte6.data.work

import androidx.annotation.Keep

@Keep
data class BingImage(
    val cid: String,
    val desc: String,
    val md5: String,
    val mid: String,
    val murl: String,
    val purl: String,
    val shkey: String,
    val t: String,
    val turl: String
)