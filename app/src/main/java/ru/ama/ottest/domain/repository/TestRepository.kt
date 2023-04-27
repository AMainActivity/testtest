package ru.ama.ottest.domain.repository

import ru.ama.ottest.domain.entity.TestInfoDomModel
import ru.ama.ottest.domain.entity.QuestionDomModel

interface TestRepository {

    fun getQuestionsInfoList(testId: Int, limit: Int): List<QuestionDomModel>

    fun getAllQuestionsListByTestId(testId: Int): List<QuestionDomModel>

    fun getTestInfo(): List<TestInfoDomModel>

    suspend fun loadTestsFromNet(): List<Int>
}
