package ru.ama.ottest.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TestListDataDto(
  @Expose
  @SerializedName("test_id") var testId: Int,
  @Expose
  @SerializedName("title") var title: String,
  @Expose
  @SerializedName("mainImageUrl" ) var mainImageUrl : String? = null,
  @Expose
  @SerializedName("minPercentOfRightAnswers") var minPercentOfRightAnswers: Int,
  @Expose
  @SerializedName("testTimeInSeconds") var testTimeInSeconds: Int,
  @Expose
  @SerializedName("countOfQuestions") var countOfQuestions: Int
)
