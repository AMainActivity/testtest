package ru.ama.ottest.domain.entity

import com.google.gson.annotations.SerializedName

data class Questions(
  @SerializedName("No.") var number: Int,
  @SerializedName("question") var question: String,
  @SerializedName("imageUrl" ) var imageUrl : String? = null,
  @SerializedName("answers") var answers: List<String>,
  @SerializedName("correct") var correct: List<Int>
)
