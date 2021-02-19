package com.bytebyte6.data.transition


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class TransResult(
    @SerializedName("dst")
    var dst: String = "",
    @SerializedName("src")
    var src: String = ""
)