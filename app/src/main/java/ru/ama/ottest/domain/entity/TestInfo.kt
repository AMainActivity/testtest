package ru.ama.ottest.domain.entity

import ru.ama.ottest.data.network.model.TestQuestionsDto
import com.google.gson.annotations.SerializedName

data class TestInfo(
    var testId: Int,
    var title: String,
    var mainImageUrl : String? = null,
    var minCountOfRightAnswers: Int,
    var minPercentOfRightAnswers: Int,
    var testTimeInSeconds: Int,
    var countOfQuestions: Int
)
