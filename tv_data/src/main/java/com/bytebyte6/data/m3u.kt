package com.bytebyte6.data

import com.bytebyte6.data.entity.Tv
import okio.internal.commonToUtf8String
import java.io.File

const val urlPattern = "(http).*?(m3u8)"
val urlRegex = Regex(urlPattern)

fun String.m3uToIpTvs(): List<Tv> {
    return File(this).m3uToIpTvs()
}

fun File.m3uToIpTvs(): List<Tv> {
    val m3uFile = this
    val m3uString = m3uFile.readBytes().commonToUtf8String()
    val containNameAndUrl = m3uString.split("#EXTINF:")
    val list = mutableListOf<Tv>()
    for (str in containNameAndUrl) {
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
            val ipTv = Tv(
                name = realName,
                url = url.value,
                tvId = 0
            )
            list.add(ipTv)
        }
    }
    return list
}