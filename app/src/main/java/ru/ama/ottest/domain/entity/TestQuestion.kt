package ru.ama.ottest.domain.entity

import com.google.gson.annotations.SerializedName

data class TestQuestion(
    var number: Int,
    var question: String,
    var imageUrl : String? = null,
    var answers: List<String>,
    var correct: List<Int>
)
