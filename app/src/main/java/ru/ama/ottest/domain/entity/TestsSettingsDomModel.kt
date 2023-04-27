package ru.ama.ottest.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestsSettingsDomModel(
    val minPercentOfRightAnswers: Int,
    val testTimeInSeconds: Int
) : Parcelable