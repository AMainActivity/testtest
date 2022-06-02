package ru.ama.ottest.data.repository

import android.app.Application
import ru.ama.ottest.data.database.TestInfoDao
import ru.ama.ottest.data.database.TestQuestionsDao
import ru.ama.ottest.data.mapper.TestMapper
import ru.ama.ottest.data.network.TestApiService
import ru.ama.ottest.domain.entity.TestInfo
import ru.ama.ottest.domain.entity.TestQuestion
import ru.ama.ottest.domain.repository.TestsRepository
import javax.inject.Inject


class TestsRepositoryImpl @Inject constructor(
    private val mapper: TestMapper,
    private val testQuestionsDao: TestQuestionsDao,
    private val testInfoDao: TestInfoDao,
    private val apiService: TestApiService,
    private val application: Application
) : TestsRepository {


    override fun getQuestionsInfoList(testId: Int, limit: Int): List<TestQuestion> {
        val list = testQuestionsDao.getQuestionListByTestId(testId, limit)
        return list.map {
            mapper.mapDbModelToEntity(it)
        }
    }

    override fun getAllQuestionsListByTestId(testId: Int): List<TestQuestion> {
        val list = testQuestionsDao.getQuestionListByTestIdAnswers(testId)
        return list.map {
            mapper.mapDbModelToEntity(it)
        }
    }


    override fun getTestInfo(): List<TestInfo> {
        return (testInfoDao.getTestInfo()).map { mapper.mapDataDbModelToEntity(it) }
    }

    override suspend fun loadTestsFromNet(): List<Int> {
        var listOfItems: MutableList<Int> = mutableListOf<Int>()
        try {
            val getJsonList = apiService.getTestList()
            if (!getJsonList.error) {
                val testDtoList = getJsonList.testListData
                val dbModelTestList = testDtoList.map {
                    mapper.mapDataDtoToDbModel(it)
                }
                val testItemsCount = testInfoDao.insertTestList(dbModelTestList)
                listOfItems.add(testItemsCount.size)
                for (testDto in testDtoList) {
                    val getJson = apiService.getTestById(testDto.testId.toString())
                    if (!getJson.error) {
                        val questionsDtoList = getJson.questions
                        val dbModelList = questionsDtoList.map {
                            mapper.mapDtoToDbModel(
                                it,
                                testDto.testId.toString()
                            )
                        }
                        val questionsItemsCount = testQuestionsDao.insertQuestionList(dbModelList)
                        listOfItems.add(questionsItemsCount.size)
                    }
                }


            }

        } catch (e: Exception) {
        }
        return listOfItems


    }


}
