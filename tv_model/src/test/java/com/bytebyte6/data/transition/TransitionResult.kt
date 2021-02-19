package com.bytebyte6.data.transition


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class TransitionResult(
    @SerializedName("Author")
    var author: Author = Author(),
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("msg")
    var msg: String = ""
)