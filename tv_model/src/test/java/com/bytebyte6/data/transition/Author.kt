package com.bytebyte6.data.transition


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Author(
    @SerializedName("desc")
    var desc: String = "",
    @SerializedName("name")
    var name: String = ""
)