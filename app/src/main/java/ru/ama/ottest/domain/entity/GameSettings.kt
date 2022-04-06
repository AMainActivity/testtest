package ru.ama.ottest.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameSettings(
    val minCountOfRightAnswers: Int,
    val minPercentOfRightAnswers: Int,
    val testTimeInSeconds: Int
) : Parcelable


/*
 Level.TEST   -> GameSettings(
                10,
                3,
                50,
                8
            )
*/