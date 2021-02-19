package com.bytebyte6.data.transition


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Data(
    @SerializedName("from")
    var from: String = "",
    @SerializedName("to")
    var to: String = "",
    @SerializedName("trans_result")
    var transResult: List<TransResult> = listOf()
)