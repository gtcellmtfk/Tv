package com.bytebyte6.data

import androidx.room.TypeConverter
import com.bytebyte6.common.GsonConfig
import com.bytebyte6.data.entity.Category
import com.bytebyte6.data.entity.Language
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class TypeConverter {

    companion object {
        val type: Type = object : TypeToken<List<Language>>() {}.type
        val typeCategory: Type = object : TypeToken<List<Category>>() {}.type

        val sType: Type = object : TypeToken<List<String>>() {}.type
    }

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapterFactory(GsonConfig.NullStringToEmptyAdapterFactory())
        .create()


    @TypeConverter
    fun toList(json: String): List<Language> {
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun toJson(list: List<Language>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun jsonToStringList(json: String): List<String> {
        return gson.fromJson(json, sType)
    }

    @TypeConverter
    fun stringListToJson(list: List<String>): String {
        return gson.toJson(list)
    }
}