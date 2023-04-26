package ru.ama.ottest.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnswerOfTest(
    val number: Int,
    val question: String,
    val imageUrl: String? = null,
    val answers: List<String>,
    val indexOfUserAnswer: List<Int>,
    val indexOfCorrect: List<Int>
) : Parcelable
