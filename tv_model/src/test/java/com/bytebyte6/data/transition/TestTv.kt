package com.bytebyte6.data.transition


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.entity.Language

@Keep
data class TestTv(
    @SerializedName("category")
    var category: String = "",
    @SerializedName("countries")
    var countries: List<Country> = listOf(),
    @SerializedName("languages")
    var languages: List<Language> = listOf(),
    @SerializedName("logo")
    var logo: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("url")
    var url: String = ""
)