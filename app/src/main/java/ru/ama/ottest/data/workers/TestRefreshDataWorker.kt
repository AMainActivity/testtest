package ru.ama.ottest.data.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import ru.ama.ottest.data.database.TestInfoDao
import ru.ama.ottest.data.database.TestQuestionsDao
import ru.ama.ottest.data.mapper.TestMapper
import ru.ama.ottest.data.network.TestApiService
import kotlinx.coroutines.delay
import javax.inject.Inject

class TestRefreshDataWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val testQuestionsDao: TestQuestionsDao,
    private val testInfoDao: TestInfoDao,
    private val apiService: TestApiService,
    private val mapper: TestMapper
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
      //  while (true) {
             try {
            val getJson = apiService.getTestList()
            if (!getJson.error) {
                val dbModelInfo = mapper.mapDataDtoToDbModel(getJson.testData)
                testInfoDao.insertTestInfo(dbModelInfo)
                Log.e("insertTestInfo",dbModelInfo.toString())
                val questionsDtoList = getJson.testData.questions
                val dbModelList = questionsDtoList.map {
                    mapper.mapDtoToDbModel(
                        it,
                        getJson.testData.testId.toString()
                    )
                }
                testQuestionsDao.insertQuestionList(dbModelList)
                        Log.e("insertQuestionList",dbModelList.toString())
            }
        return Result.success()
              } catch (e: Exception) {return Result.failure()
             }
          //      delay(10000)
        //}
    }

    companion object {

        const val NAME = "TestRefreshDataWorker"

        fun makeRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<TestRefreshDataWorker>().build()
        }
    }

    class Factory @Inject constructor(
        private val testQuestionsDao: TestQuestionsDao,
        private val testInfoDao: TestInfoDao,
        private val apiService: TestApiService,
        private val mapper: TestMapper
    ) : ChildWorkerFactory {

        override fun create(
            context: Context,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return TestRefreshDataWorker(
                context,
                workerParameters,
                testQuestionsDao,
                testInfoDao,
                apiService,
                mapper
            )
        }
    }
}
