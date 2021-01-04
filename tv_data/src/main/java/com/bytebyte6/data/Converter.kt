package com.bytebyte6.data

import android.content.Context
import androidx.room.TypeConverter
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.model.Language
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


class Converter {

    val gson: Gson = GsonBuilder()
        .registerTypeAdapterFactory(GsonConfig.NullStringToEmptyAdapterFactory())
        .create()

    private val type = object : TypeToken<List<Language>>() {}.type

    @TypeConverter
    fun toList(json: String): List<Language> {
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun toJson(list: List<Language>): String {
        return gson.toJson(list)
    }

    fun getTvs(context: Context): List<Tv> {
        val json: String = context.assets.open("channels.json")
            .bufferedReader()
            .use { it.readText() }
        return gson.fromJson(json, object : TypeToken<List<Tv>>() {}.type)
    }
}