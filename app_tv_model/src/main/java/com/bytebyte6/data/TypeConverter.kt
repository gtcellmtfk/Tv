package com.bytebyte6.data

import androidx.room.TypeConverter
import com.bytebyte6.base.GsonConfig
import com.bytebyte6.data.model.Language
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class TypeConverter {

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapterFactory(com.bytebyte6.base.GsonConfig.NullStringToEmptyAdapterFactory())
        .create()

    private val type = object : TypeToken<List<Language>>() {}.type

    private val sType = object : TypeToken<List<String>>() {}.type

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