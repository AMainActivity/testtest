package ru.ama.ottest.domain.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TestQuestion
(
    //var _id: Int,
    var number: Int,
    var question: String,
    var imageUrl : String? = null,
    var answers: List<String>,
    var correct: List<Int>
): Parcelable
