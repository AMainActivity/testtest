package ru.ama.ottest.data.repository

import android.R.attr.tag
import android.app.Application
import android.util.Log
import androidx.lifecycle.Observer
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.common.util.concurrent.ListenableFuture
import ru.ama.ottest.data.database.TestInfoDao
import ru.ama.ottest.data.database.TestQuestionsDao
import ru.ama.ottest.data.mapper.TestMapper
import ru.ama.ottest.data.network.TestApiService
import ru.ama.ottest.domain.entity.*
import ru.ama.ottest.domain.repository.GameRepository
import javax.inject.Inject


class GameRepositoryImpl @Inject constructor(
    private val mapper: TestMapper,
    private val testQuestionsDao: TestQuestionsDao,
    private val testInfoDao: TestInfoDao,
    private val apiService: TestApiService,
    private val application: Application
) : GameRepository {
	


    override fun getQuestionsInfoList(testId: Int,limit:Int): List<TestQuestion>{
        var rl:MutableList<TestQuestion> = mutableListOf<TestQuestion>()
        for (l in testQuestionsDao.getQuestionListByTestId(testId,limit))
        {
            rl.add(mapper.mapDbModelToEntity(l))
        }
        Log.e("getQuestionsrl","${testId} ${limit} ${rl.toString()}")
        return rl

    }
   /*  fun getQuestionsInfoList2(): LiveData<List<TestQuestion>> {
        return Transformations.map(testQuestionsDao.getQuestionList()) {
            it.map {
                mapper.mapDbModelToEntity(it)
            }
        }
    }*/

    override fun getTestInfo(testId:Int): List<TestInfo> {
        Log.e("getTestInfo1",testInfoDao.toString())
        Log.e("getTestInfo",testInfoDao.getTestInfo(testId).toString())
       // if(testInfoDao.getTestInfo().value!=null)
        var rl:MutableList<TestInfo> = mutableListOf<TestInfo>()
        for (l in testInfoDao.getTestInfo(testId))
        {
            rl.add(mapper.mapDataDbModelToEntity(l))
        }
        return rl
       // return  mapper.mapDataDbModelToEntity(testInfoDao.getTestInfo(testId))
    }

    override suspend fun loadData():List<Int>  {
        var listOfItems:MutableList<Int> = mutableListOf<Int>()
		try {
            val getJsonList = apiService.getTestList()
				 if (!getJsonList.error)
				{
					val testDtoList = getJsonList.testListData					
					val dbModelTestList = testDtoList.map {
                    mapper.mapDataDtoToDbModel(it)
                }
					val testItemsCount= testInfoDao.insertTestList(dbModelTestList)
					listOfItems.add(testItemsCount.size)
					 Log.e("insertTestInfo",dbModelTestList.toString())
					 
					    val getJson = apiService.getTestById()
				if (!getJson.error) {
               // val dbModelInfo = mapper.mapDataDtoToDbModel(getJsonList.TestListDataDto)
               // testInfoDao.insertTestInfo(dbModelInfo)
               // Log.e("insertTestInfo",dbModelInfo.toString())
				
				
                val questionsDtoList = getJson.testData.questions					
                val dbModelList = questionsDtoList.map {
                    mapper.mapDtoToDbModel(
                        it,
                        getJson.testData.testId.toString()
                    )
                }
                val questionsItemsCount=testQuestionsDao.insertQuestionList(dbModelList)
				listOfItems.add(questionsItemsCount.size)
                        Log.e("insertQuestionList",dbModelList.toString())
            }
					 
				 }
                
              } catch (e: Exception) {}
			  return listOfItems
		
     /*   val workManager = WorkManager.getInstance(application)

        workManager.enqueueUniqueWork(
            TestRefreshDataWorker.NAME,
            ExistingWorkPolicy.REPLACE,
            TestRefreshDataWorker.makeRequest()
        )*/
     
    }



}
