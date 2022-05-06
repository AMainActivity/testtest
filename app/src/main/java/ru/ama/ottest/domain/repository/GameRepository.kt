package ru.ama.ottest.domain.repository

import androidx.lifecycle.LiveData
import ru.ama.ottest.domain.entity.*

interface GameRepository {

    fun generateQuestion(questionNo:Int): Questions

    fun getGameSettings(): GameSettings
	
    //fun getTestInfo(): MainTest
	
    fun shuffleListOfQuestions()
	
    fun getQuestionsInfoList(): LiveData<List<TestQuestion>>

    fun getTestInfo():LiveData<List<TestInfo>>

    fun loadData()
}
