package ru.ama.ottest.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TestListDataDto(
    @Expose
    @SerializedName("test_id") val testId: Int,
    @Expose
    @SerializedName("title") val title: String,
    @Expose
    @SerializedName("mainImageUrl") val mainImageUrl: String? = null,
    @Expose
    @SerializedName("minPercentOfRightAnswers") val minPercentOfRightAnswers: Int,
    @Expose
    @SerializedName("testTimeInSeconds") val testTimeInSeconds: Int,
    @Expose
    @SerializedName("countOfQuestions") val countOfQuestions: Int
)
