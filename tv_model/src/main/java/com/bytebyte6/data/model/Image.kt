package com.bytebyte6.data.model

import androidx.annotation.Keep

@Keep
interface Image {
    val id:Long
    var logo: String
    var name: String
    var videoUrl: String
    var favorite: Boolean
    var download: Boolean
    val transitionName: String
}
