package com.bytebyte6.data.model

import androidx.annotation.Keep

@Keep
interface Card {
    val title: String
    val body: String
    val outline: Boolean
    val color: Int
    val radius: Int
    val transitionName:String
}
