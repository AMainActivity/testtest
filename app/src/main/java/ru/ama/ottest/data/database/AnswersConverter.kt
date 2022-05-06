package ru.ama.ottest.data.database

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import java.util.stream.Collectors

class AnswersConverter {

    @TypeConverter
    fun fromAnswers(answers: List<String>): String {
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.toJson(answers, type)
    }

    @TypeConverter
    fun toAnswers(value: String): List<String> {
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }

    /*@RequiresApi(Build.VERSION_CODES.N)
    @TypeConverter
    fun fromAnswers(answers: List<String?>): String? {
        return answers.stream().collect(Collectors.joining(","))
    }

    @TypeConverter
    fun toAnswers(data: String): List<String?>? {
        return Arrays.asList(data.split(",").toTypedArray())
    }*/
    }