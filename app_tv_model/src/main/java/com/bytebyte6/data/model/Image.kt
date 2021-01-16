package com.bytebyte6.data.model

import androidx.annotation.Keep

@Keep
interface Image {
    var imageUrl: String
    var title: String
    var videoUrl: String
    var love: Boolean
}
