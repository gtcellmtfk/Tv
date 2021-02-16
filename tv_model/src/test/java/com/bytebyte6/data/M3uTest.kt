package com.bytebyte6.data

import org.junit.Test
import java.io.File

class M3uTest {
    @Test
    fun test1(){
        println(File("..","pic").absolutePath)
    }
    @Test
    fun list() {
        val modelDir = File("..", "channels")
        val listFiles = modelDir.listFiles()
        listFiles?.forEach { file ->
            val tvs = M3u.getTvs(file)
            println("${file.name} size = ${tvs.size}")
            tvs.forEach {
                assert(it.name.isNotEmpty())
                assert(it.countryCode.isNotEmpty())
                assert(it.url.isNotEmpty())
                assert(it.language.isNotEmpty())
                assert(it.category.isNotEmpty())
            }
        }
        println("Total File: = ${listFiles?.size}")
    }
}