package com.bytebyte6.data

import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Test

class TestOkhttp {
    @Test
    fun test_okhttp() {
        val url = "https://iptv-org.github.io/iptv/categories/auto.m3u"
        val okHttpClient = OkHttpClient()
        val request = Request.Builder().get().url(url).build()
        val string = okHttpClient.newCall(request).execute().body?.string()
        println(string)
    }
}
