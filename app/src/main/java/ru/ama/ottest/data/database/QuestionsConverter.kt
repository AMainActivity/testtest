package ru.ama.ottest.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.ama.ottest.data.network.model.TestQuestionsDto

class QuestionsConverter {

    @TypeConverter
    fun fromQuestions(Correct: List<TestQuestionsDto>): String {
        val gson = Gson()
        val type = object : TypeToken<List<TestQuestionsDto>>() {}.type
        return gson.toJson(Correct, type)
    }

    @TypeConverter
    fun toQuestions(value: String): List<TestQuestionsDto> {
        val gson = Gson()
        val type = object : TypeToken<List<TestQuestionsDto>>() {}.type
        return gson.fromJson(value, type)
    }


}