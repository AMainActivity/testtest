package ru.ama.ottest.domain.repository

import ru.ama.ottest.domain.entity.*

interface GameRepository {

    fun generateQuestion(questionNo:Int): Questions

    fun getGameSettings(): GameSettings
	
    fun getTestInfo(): MainTest
	
    fun shuffleListOfQuestions()
}
