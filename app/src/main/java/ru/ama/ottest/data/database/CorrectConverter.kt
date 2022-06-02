package ru.ama.ottest.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CorrectConverter {

    @TypeConverter
    fun fromCorrect(Correct: List<Int>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.toJson(Correct, type)
    }

    @TypeConverter
    fun toCorrect(value: String): List<Int> {
        val gson = Gson()
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(value, type)
    }


}