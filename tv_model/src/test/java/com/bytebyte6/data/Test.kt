package com.bytebyte6.data

import com.bytebyte6.common.GsonConfig
import com.bytebyte6.data.entity.Category
import com.bytebyte6.data.entity.Tv
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.junit.Test
import java.io.File

class Test2 {
    private val paths = listOf(
        "C:\\Users\\zacks\\Videos\\zho.m3u",
        "C:\\Users\\zacks\\Videos\\china.m3u8",
        "C:\\Users\\zacks\\Videos\\test.m3u",
        "C:\\Users\\zacks\\Videos\\test2.m3u",
        "C:\\Users\\zacks\\Videos\\央视+卫视+NewTV广西移动源.m3u",
        "C:\\Users\\zacks\\Videos\\mov.m3u",
        "C:\\Users\\zacks\\Videos\\kor.m3u",
        "C:\\Users\\zacks\\Videos\\index.m3u",
        "C:\\Users\\zacks\\Videos\\aqy.m3u",
        "C:\\Users\\zacks\\Videos\\111.m3u"
    )

    @Test
    fun test11() {
//        val gson=Gson()
//        val list= listOf(
//            Category("A","A Desc"),
//            Category("B","B Desc"),
//            Category("C","C Desc")
//        )
//        println(gson.toJson(list))
        //[{"color":0,"category":"A","desc":"A Desc"},{"color":0,"category":"B","desc":"B Desc"},{"color":0,"category":"C","desc":"C Desc"}]
    }

    @Test
    fun test2() {
        val file = File(paths[0])
        M3u.getTvs(file).forEach {
            println(it.name)
            println(it.url)
        }
    }

    @Test
    fun test4() {
        val file = File(paths[1])
        M3u.getTvs(file).forEach {
            println(it.name)
            println(it.url)
        }
    }

    @Test
    fun test5() {
        val file = File(paths[2])
        M3u.getTvs(file).forEach {
            println(it.name)
            println(it.url)
        }
    }

    @Test
    fun test3() {
        val file = File(paths[3])
        M3u.getTvs(file).forEach {
            println(it.name)
            println(it.url)
        }
    }

    @Test
    fun test7() {
        val file = File(paths[4])
        M3u.getTvs(file).forEach {
            println(it.name)
            println(it.url)
        }
    }

    @Test
    fun test6() {
        val file = File(paths[5])
        M3u.getTvs(file).forEach {
            println(it.name)
            println(it.url)
        }
    }

    @Test
    fun test8() {
        val file = File(paths[6])
        M3u.getTvs(file).forEach {
            println(it.name)
            println(it.url)
        }
    }

    @Test
    fun test9() {
        val file = File(paths[7])
        M3u.getTvs(file).forEach {
            println(it.name)
            println(it.url)
        }
    }

    @Test
    fun test10() {
        val file = File(paths[8])
        M3u.getTvs(file).forEach {
            println(it.name)
            println(it.url)
        }
    }
}