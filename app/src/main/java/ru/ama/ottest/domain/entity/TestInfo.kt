package ru.ama.ottest.domain.entity

import android.os.Parcelable
import ru.ama.ottest.data.network.model.TestQuestionsDto
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TestInfo(
    var testId: Int,
    var title: String,
    var mainImageUrl : String? = null,
    var minPercentOfRightAnswers: Int,
    var testTimeInSeconds: Int,
    var countOfQuestions: Int
) : Parcelable
