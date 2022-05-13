package ru.ama.ottest.domain.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResultOfTest(
    var number: Int,
    var question: String,
    var imageUrl : String? = null,
    var answer: String,
    var correct: String
): Parcelable
