package ru.ama.ottest.domain.repository

import ru.ama.ottest.domain.entity.*

interface GameRepository {

    fun generateQuestion(): Questions

    fun getGameSettings(): GameSettings
    fun getTestInfo(): MainTest
    fun getCurrentNoOfQuestion(): Int
}
