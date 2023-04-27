package ru.ama.ottest.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuestionDomModel
    (
    val number: Int,
    val question: String,
    val imageUrl: String? = null,
    val answers: List<String>,
    val correct: List<Int>
) : Parcelable
