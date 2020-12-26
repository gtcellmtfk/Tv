package com.bytebyte6.data

import androidx.room.TypeConverter
import com.bytebyte6.data.model.Language
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converter {
    @TypeConverter
    fun toList(json: String): List<Language> {
        return Gson().fromJson(json, object : TypeToken<List<Language>>() {}.type)
    }

    @TypeConverter
    fun toJson(list: List<Language>): String {
        return Gson().toJson(list)
    }
}