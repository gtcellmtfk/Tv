package com.bytebyte6.data

import com.bytebyte6.data.GsonConfig.NullStringToEmptyAdapterFactory
import com.bytebyte6.data.entity.Tv
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.junit.Assert.assertEquals
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun test(){
        val list= mutableListOf<String>()
        list.add("1")
        list.add("2")
        list.add("3")
        list.add("4")
        list.add("5")

        val list2= mutableListOf<String>()
        list2.add("2")
        list2.add("5")

        println(list.containsAll(list2))
    }

    @Test
    fun addition_isCorrect() {

        val json = "{\n" +
                "    \"name\": null" +
                "}"

        val gson = Gson()

        val gson2 =
            GsonBuilder().registerTypeAdapterFactory(NullStringToEmptyAdapterFactory()).create()

        val iptv = gson2.fromJson<Tv>(json, Tv::class.java)

        println(iptv.name==null)
        println(iptv.name+"1")
        println(iptv)

        println(gson2.toJson(iptv))

        assertEquals(4, 2 + 2)

    }
}