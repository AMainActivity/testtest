package ru.ama.ottest.domain.repository

import ru.ama.ottest.domain.entity.TestInfo
import ru.ama.ottest.domain.entity.TestQuestion

interface TestsRepository {

    fun getQuestionsInfoList(testId: Int, limit: Int): List<TestQuestion>

    fun getAllQuestionsListByTestId(testId: Int): List<TestQuestion>

    fun getTestInfo(): List<TestInfo>

    suspend fun loadTestsFromNet(): List<Int>
}
