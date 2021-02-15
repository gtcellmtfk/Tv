package com.bytebyte6.data

import org.junit.Test
import java.io.File

class M3uTest {
    @Test
    fun list() {
        val modelDir = File(".", "channels")
        val listFiles = modelDir.listFiles()
        listFiles?.forEach { file ->
            println(file.name)
            val tvs = M3u.getTvs(file)
            println("Tvs Size = ${tvs.size}")
            tvs.forEach {
                assert(it.name.isNotEmpty())
                assert(it.countryCode.isNotEmpty())
                assert(it.url.isNotEmpty())
                assert(it.language.isNotEmpty())
                assert(it.category.isNotEmpty())
            }
        }
        println("File size = ${listFiles?.size}")
    }
}