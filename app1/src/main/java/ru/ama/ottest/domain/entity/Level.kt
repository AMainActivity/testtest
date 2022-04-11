package ru.ama.ottest.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Level : Parcelable {

    EASY, NORMAL, HARD, TEST
}
