package com.bytebyte6.data

import com.bytebyte6.data.entity.Category
import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.entity.Language
import com.bytebyte6.data.entity.Tv
import okio.internal.commonToUtf8String
import java.io.File
import java.io.InputStream
import java.util.*

object M3u {
    private const val logoPattern = "(?<=tvg-logo=\").*?(?=\")"
    private const val namePattern = "(?<=tvg-name=\").*?(?=\")"
    private const val namePattern3 = "(?<=\",).*?(?=\n)"
    private const val languagePattern = "(?<=tvg-language=\").*?(?=\")"
    private const val countryPattern = "(?<=tvg-country=\").*?(?=\")"
    private const val categoryPattern = "(?<=group-title=\").*?(?=\")"
    private val logoRegex = Regex(logoPattern)
    private val nameRegex = Regex(namePattern)
    private val nameRegex3 = Regex(namePattern3)
    private val langRegex = Regex(languagePattern)
    private val countryRegex = Regex(countryPattern)
    private val categoryRegex = Regex(categoryPattern)

    /**
     * 从inputStream读取
     * @param inputStream
     */
    fun getTvs(inputStream: InputStream): List<Tv> {
        return getTvs(getM3uString(inputStream.readBytes()))
    }

    /**
     * 从文件去读
     * @param m3uFile xxx.m3u File
     */
    fun getTvs(m3uFile: File): List<Tv> {
        return getTvs(getM3uString(m3uFile.readBytes()))
    }

    /**
     * 对得到的字符串做处理
     */
    private fun getM3uString(byteArray: ByteArray): String {
        return byteArray.commonToUtf8String()
            .removePrefix("#EXTM3U")
            .replace("\r\n", "\n")
            .trim()
    }

    /**
     * 分割字符串，去除空字符，按照不同类型的文件解析
     */
    private fun getTvs(m3uString: String): List<Tv> {
        // 来自github 开源的m3u文件
        val fromGithub = m3uString.contains("tvg-id")
        val mutableList = if (fromGithub) {
            m3uString.split("#EXTINF:-1 ")
        } else {
            m3uString.split("#EXTINF:-1 ,")
        }.toMutableList()
        // 去除空字符
        mutableList.remove("")
        return if (fromGithub) {
            getTvsByTvg(mutableList)
        } else {
            getTvsNormal(mutableList)
        }
    }

    /**
     * 解析标准m3u文件
     */
    private fun getTvsNormal(list: List<String>): List<Tv> {
        val tvs = mutableListOf<Tv>()
        for (str in list) {
            val nameAndUrl = str.split("\n")
            tvs.add(
                Tv(
                    name = nameAndUrl[0],
                    url = nameAndUrl[1].trim(),
                    countryCode = Country.UNSORTED_LOW,
                    language = Language.UNKOWN,
                    category = Category.OTHER
                )
            )
        }
        return tvs
    }

    /**
     * 解析来自github的m3u文件
     */
    private fun getTvsByTvg(list: List<String>): List<Tv> {
        val tvs = mutableListOf<Tv>()
        for (str in list) {
            // #EXTVLCOPT为特殊情况
            val url = if (str.contains("#EXTVLCOPT")) {
                str.split("\n")[2].trim()
            } else {
                str.split("\n")[1].trim()
            }

            if (url.isEmpty()) {
                println("url.isEmpty() continue")
                continue
            }

            val logo = logoRegex.find(str)?.value ?: ""

            var name = nameRegex3.find(str)?.value
            if (name.isNullOrEmpty()) {
                name = nameRegex.find(str)?.value
            }
            if (name!!.contains("&")) {
                name = name.replace("&", "")
            }

            var lang = langRegex.find(str)?.value
            if (lang.isNullOrEmpty()) {
                lang = Language.UNKOWN
            }

            var code = countryRegex.find(str)?.value
            if (code.isNullOrEmpty()) {
                code = Country.UNSORTED
            }

            var category = categoryRegex.find(str)?.value
            if (category.isNullOrEmpty()) {
                category = Category.OTHER
            }

            tvs.add(Tv().apply {
                this.url = url
                this.logo = logo
                this.name = name
                this.language = lang
                this.countryCode = code.toLowerCase(Locale.ROOT)
                this.category = category
            })
        }
        return tvs
    }
}
