package com.bytebyte6.data

import androidx.room.TypeConverter
import com.bytebyte6.data.model.Language
import com.bytebyte6.data.model.WrapLanguages
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class Converter {
    private val gson = Gson()
    private val type = object : TypeToken<List<Language>>() {}.type

    @TypeConverter
    fun toList(json: String): List<Language> {
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun toJson(list: List<Language>): String {
        return gson.toJson(list)
    }

    //    @TypeConverter
//    fun toLanguages(list: List<String>): List<Language> {
//        val languages = ArrayList<Language>(list.size)
//        for (json in list) {
//            try {
//                languages.addAll(toList(json))
//            } catch (e: Exception) {
//                languages.add(Language("", ""))
//                println("empty,add default Object")
//            }
//        }
//        return languages
//    }
//
//    @TypeConverter
//    fun toLanguages(list: List<WrapLanguages>): List<Language> {
//        val languages = mutableSetOf<Language>()
//        for (ls in list) {
//            languages.addAll(ls.language)
//        }
//        return languages.sorted()
//    }
}