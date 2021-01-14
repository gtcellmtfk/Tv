package com.bytebyte6.data.model

import androidx.annotation.Keep

@Keep
interface Image {
    val imageUrl: String
    val title: String
    val videoUrl: String
}
