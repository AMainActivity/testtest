package ru.ama.ottest.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameResult(
    val winner: Boolean,
    val countOfRightAnswers: Int,
    val countOfQuestions: Int,
    val gameSettings: GameSettings
) : Parcelable {

    val percentageOfRightAnswers: Int
        get() = if (countOfQuestions > 0) {
            ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
        } else {
            0
        }
}
