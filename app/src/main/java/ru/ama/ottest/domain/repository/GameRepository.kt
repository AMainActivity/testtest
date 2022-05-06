package ru.ama.ottest.domain.repository

import androidx.lifecycle.LiveData
import ru.ama.ottest.domain.entity.*

interface GameRepository {

    fun generateQuestion(questionNo:Int): Questions

    fun getGameSettings(): GameSettings
	
    //fun getTestInfo(): MainTest
	
    fun shuffleListOfQuestions()
	
    fun getQuestionsInfoList(testId:Int): List<TestQuestion>

    fun getTestInfo(testId:Int):TestInfo

    fun loadData()
}
