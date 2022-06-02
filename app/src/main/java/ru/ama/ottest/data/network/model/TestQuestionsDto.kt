package ru.ama.ottest.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TestQuestionsDto(
    @Expose
    @SerializedName("No.") val number: Int,
    @Expose
    @SerializedName("question") val question: String,
    @Expose
    @SerializedName("imageUrl") val imageUrl: String? = null,
    @Expose
    @SerializedName("answers") val answers: List<String>,
    @Expose
    @SerializedName("correct") val correct: List<Int>
)
