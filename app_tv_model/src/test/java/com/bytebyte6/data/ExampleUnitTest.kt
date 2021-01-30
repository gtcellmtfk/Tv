package com.bytebyte6.data

import com.google.gson.Gson
import com.google.gson.JsonObject
import okio.internal.commonToUtf8String
import org.junit.Test
import java.io.File

class ExampleUnitTest {

    private val namePattern = "(?<=tvg-name=\").*?(?=\")"
    private val languagePattern = "(?<=tvg-language=\").*?(?=\")"
    private val logoPattern = "(?<=tvg-logo=\").*?(?=\")"
    private val urlPattern = "(http).*?(m3u8)"
    private val countryPattern = "(?<=tvg-country=\").*?(?=\")"
    private val titlePattern = "(?<=tvg-title=\").*?(?=\")"

    private val urlRegex = Regex(urlPattern)
    private val nameRegex = Regex(namePattern)
    private val languageRegex = Regex(languagePattern)
    private val logoRegex = Regex(logoPattern)
    private val countryRegex = Regex(countryPattern)
    private val titleRegex = Regex(titlePattern)

    @Test
    fun test2() {
        val path = "C:\\Users\\zacks\\Videos\\zho.m3u"
        val m3uFile = File(path)
        println(m3uFile.m3uToIpTvs())
    }

    //匹配两个字符串A与B中间的字符串包含A与B：
    //表达式: A.*?B（“.“表示任意字符，“？”表示匹配0个或多个）
    //匹配两个字符串A与B中间的字符串包含A但是不包含B：
    //表达式: A.*?(?=B)
    //匹配两个字符串A与B中间的字符串且不包含A与B：
    //表达式: (?<=A).*?(?=B)

    @Test
    fun test() {
        val path = "C:\\Users\\zacks\\Videos\\index.m3u"
        val m3uFile = File(path)
        val m3uString = m3uFile.readBytes().commonToUtf8String()

        val nameResult = nameRegex.findAll(m3uString).count()
        val languageResult = languageRegex.findAll(m3uString).count()
        val logoResult = logoRegex.findAll(m3uString).count()
        val urlResult = urlRegex.findAll(m3uString).count()
        val countryResult = countryRegex.findAll(m3uString).count()
        val titleResult = titleRegex.findAll(m3uString).count()

        val gson= Gson().fromJson<Any>("",Any::class.java)
        val obj=JsonObject()
    }
}