package ru.ama.ottest.domain.entity

import com.google.gson.annotations.SerializedName

data class MainTest(
  @SerializedName("title") var title: String,
  @SerializedName("mainImageUrl" ) var mainImageUrl : String? = null,
  @SerializedName("minCountOfRightAnswers") var minCountOfRightAnswers: Int,
  @SerializedName("minPercentOfRightAnswers") var minPercentOfRightAnswers: Int,
  @SerializedName("testTimeInSeconds") var testTimeInSeconds: Int,
  @SerializedName("questions") var questions: List<Questions>
)
