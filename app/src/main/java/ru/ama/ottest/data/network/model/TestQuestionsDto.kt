package ru.ama.ottest.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TestQuestionsDto(
  @Expose
  @SerializedName("No.") var number: Int,
  @Expose
  @SerializedName("question") var question: String,
  @Expose
  @SerializedName("imageUrl" ) var imageUrl : String? = null,
  @Expose
  @SerializedName("answers") var answers: List<String>,
  @Expose
  @SerializedName("correct") var correct: List<Int>
)
