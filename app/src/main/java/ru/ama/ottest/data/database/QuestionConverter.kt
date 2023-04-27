package ru.ama.ottest.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.ama.ottest.data.network.model.QuestionDto

class QuestionConverter {

    @TypeConverter
    fun fromQuestions(Correct: List<QuestionDto>): String {
        val gson = Gson()
        val type = object : TypeToken<List<QuestionDto>>() {}.type
        return gson.toJson(Correct, type)
    }

    @TypeConverter
    fun toQuestions(value: String): List<QuestionDto> {
        val gson = Gson()
        val type = object : TypeToken<List<QuestionDto>>() {}.type
        return gson.fromJson(value, type)
    }


}