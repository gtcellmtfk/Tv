package com.bytebyte6.data

import org.junit.Test
import java.io.File
import java.util.*

class M3uTest {

    @Test
    fun test_Thread() {
        var stop = false

        Thread {
            println(Thread.currentThread().name)
            while (!stop) {
                println("ing...")
            }
            println("stop")
        }.start()

        for (i in 0..Int.MAX_VALUE) {
            if (i >= Int.MAX_VALUE / 2) {
                stop = true
                println(UUID.randomUUID().toString())
                break
            }
        }
        println("2 " + Thread.currentThread().name)
    }

    @Test
    fun test() {
        println(File("..", "pic").absolutePath)
    }

    @Test
    fun test_m3u_parse() {
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