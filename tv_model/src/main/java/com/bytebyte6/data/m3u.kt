package com.bytebyte6.data

import com.bytebyte6.data.entity.Tv
import okio.internal.commonToUtf8String
import java.io.File
import java.io.InputStream

private const val urlPattern = "(http).*?(m3u8)"
private val urlRegex = Regex(urlPattern)
private const val logoPattern = "(?<=tvg-logo=\").*?(?=\")"
private val logoRegex = Regex(logoPattern)

fun InputStream.toTvs():List<Tv>{
    val containNameAndUrlList = this.readBytes().commonToUtf8String().split("#EXTINF:")
    val list = mutableListOf<Tv>()
    for (str in containNameAndUrlList) {
        val nameAndUrl: String
        val strs = str.split(",")
        nameAndUrl = if (strs.size > 1) {
            strs[1]
        } else {
            strs[0]
        }

        val realName = nameAndUrl.split("\n")[0]
        val url = urlRegex.find(nameAndUrl)

        if (url != null) {
            val logo = logoRegex.find(str)?.value
            val ipTv = Tv(
                name = realName,
                url = url.value,
                logo = logo ?: ""
            )
            list.add(ipTv)
        }
    }
    return list
}

fun String.m3uToIpTvs(): List<Tv> {
    return File(this).m3uToIpTvs()
}

fun File.m3uToIpTvs(): List<Tv> {
    val m3uFile = this
    val m3uString = m3uFile.readBytes().commonToUtf8String()
    val containNameAndUrlList = m3uString.split("#EXTINF:")
    val list = mutableListOf<Tv>()
    for (str in containNameAndUrlList) {
        val nameAndUrl: String
        val strs = str.split(",")
        nameAndUrl = if (strs.size > 1) {
            strs[1]
        } else {
            strs[0]
        }

        val realName = nameAndUrl.split("\n")[0]
        val url = urlRegex.find(nameAndUrl)

        if (url != null) {
            val logo = logoRegex.find(str)?.value
            val ipTv = Tv(
                name = realName,
                url = url.value,
                logo = logo ?: ""
            )
            list.add(ipTv)
        }
    }
    return list
}