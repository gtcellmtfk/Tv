package com.bytebyte6.data.transition

import com.bytebyte6.common.GsonConfig
import com.bytebyte6.data.entity.Category
import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.entity.Language
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Test
import java.io.File

class Trans {
    private val nullStringToEmptyAdapterFactory = GsonConfig.NullStringToEmptyAdapterFactory()
    private val gson =
        GsonBuilder().registerTypeAdapterFactory(nullStringToEmptyAdapterFactory).create()
    private val file = File(".","channels.json")
    private val json = file.readText()
    private val cs = mutableSetOf<Country>()
    private val cas = mutableSetOf<Category>()
    private val ls = mutableSetOf<Language>()
    private val split = "\n"
    private val tvs: List<TestTv> = gson.fromJson(json, object : TypeToken<List<TestTv>>() {}.type)
    private val okHttpClient = OkHttpClient()

    @Test
    fun trans_cs() {
        tvs.forEach {
            cs.addAll(it.countries)
        }
        println("cs size = ${cs.size}")
        val csKeysBuilder = StringBuilder()
        cs.forEach {
            csKeysBuilder.append(it.name).append(split)
        }
        println("cs: $csKeysBuilder")
        val csKeysFile = File(".", "csKeys.txt")
        if (!csKeysFile.exists()) {
            csKeysFile.writeText(csKeysBuilder.toString())
        }
        val dstCs = csKeysFile.readText().split(split)
        println("dstCs size = ${dstCs.size}")
        cs.forEachIndexed { index, country ->
            try {
                country.nameChinese = dstCs[index]
            } catch (e: Exception) {
                assert(false)
            }
        }
        val csFile = File("src/main/assets", "countries.json")
        csFile.writeText(gson.toJson(cs))
    }

    @Test
    fun trans_cas() {
        val split=","
        tvs.forEach {
            if (it.category.isEmpty()) {
                it.category = Category.OTHER
            }
            cas.add(Category(it.category))
        }
        println("cas size = ${cas.size}")
        val casKeysBuilder = StringBuilder()
        cas.forEach {
            casKeysBuilder.append(it.category).append(split)
        }
        println("cas: $casKeysBuilder")
        val urlCas = "https://v1.alapi.cn/api/fanyi?q=$casKeysBuilder&from=en&to=zh"
        val requestCas = Request.Builder().url(urlCas).build()
        val stringCas = okHttpClient.newCall(requestCas).execute()
            .body!!.string()
            .replace("，", ",")
            .replace("、", ",")
        println("stringCas=$stringCas")
        val resultCas = gson.fromJson(stringCas, TransitionResult::class.java)
        val dstCas = resultCas.data.transResult[0].dst.split(split)
        println("dstCas size = ${dstCas.size}")
        cas.forEachIndexed { index, category ->
            try {
                category.categoryChinese = dstCas[index]
            } catch (e: Exception) {
                assert(false)
            }
        }
        val casFile = File("src/main/assets", "categories.json")
        casFile.writeText(gson.toJson(cas))
    }

    @Test
    fun trans_ls() {
        tvs.forEach {
            ls.addAll(it.languages)
        }
        println("ls size = ${ls.size}")
        val lsKeysBuilder = StringBuilder()
        ls.forEach {
            lsKeysBuilder.append(it.languageName)
                .append(split)
        }
        println("ls: $lsKeysBuilder")
        val lsKeysFile= File(".","lsKeys.txt")
        if (!lsKeysFile.exists()){
            lsKeysFile.writeText(lsKeysBuilder.toString())
        }
        val dstLs = lsKeysFile.readText().split(split)
        println("dstLs size = ${dstLs.size}")
        ls.forEachIndexed { index, language ->
            try {
                language.languageNameChinese = dstLs[index]
            } catch (e: Exception) {
                assert(false)
            }
        }
        val lsFile = File("src/main/assets", "languages.json")
        lsFile.writeText(gson.toJson(ls))
    }
}