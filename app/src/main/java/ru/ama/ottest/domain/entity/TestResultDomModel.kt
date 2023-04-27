package ru.ama.ottest.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestResultDomModel(
    val title: String,
    val timeForTest: String,
    val countOfAnswers: Int,
    val isWin: Boolean,
    val countOfRightAnswers: Int,
    val countOfQuestions: Int,
    val minPercentOfRightAnswers: Int,
    val testTimeInSeconds: Int,
    val userAnswerDomModel: List<UserAnswerDomModel>
) : Parcelable {

    companion object {

        private const val STO_PROCENTOV = 100
    }

    val percentageOfRightAnswers: Int
        get() = if (countOfQuestions > 0) {
            ((countOfRightAnswers / countOfQuestions.toDouble()) * STO_PROCENTOV).toInt()
        } else {
            0
        }
}
