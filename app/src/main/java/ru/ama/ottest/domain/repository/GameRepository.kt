package ru.ama.ottest.domain.repository

import ru.ama.ottest.domain.entity.GameSettings
import ru.ama.ottest.domain.entity.Level
import ru.ama.ottest.domain.entity.Question
import ru.ama.ottest.domain.entity.Questions

interface GameRepository {

    fun generateQuestion(): Questions

    fun getGameSettings(): GameSettings
}
