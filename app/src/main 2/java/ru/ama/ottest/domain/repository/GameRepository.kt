package ru.ama.ottest.domain.repository

import ru.ama.ottest.domain.entity.GameSettings
import ru.ama.ottest.domain.entity.Level
import ru.ama.ottest.domain.entity.Question

interface GameRepository {

    fun generateQuestion(
        maxValue: Int,
        minSumValue: Int,
        minAnswerValue: Int,
        countOfOptions: Int
    ): Question

    fun getGameSettings(level: Level): GameSettings
}
