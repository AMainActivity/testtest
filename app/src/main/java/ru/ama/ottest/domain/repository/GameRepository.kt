package ru.ama.ottest.domain.repository

import androidx.lifecycle.LiveData
import ru.ama.ottest.domain.entity.*

interface GameRepository {
	
    fun getQuestionsInfoList(testId:Int,limit:Int): List<TestQuestion>

    fun getTestInfo(testId:Int):List<TestInfo>

    suspend fun loadData():List<Int>
}
