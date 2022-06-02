package ru.ama.ottest.data.repository

import android.app.Application
import android.util.Log
import ru.ama.ottest.data.database.TestInfoDao
import ru.ama.ottest.data.database.TestQuestionsDao
import ru.ama.ottest.data.mapper.TestMapper
import ru.ama.ottest.data.network.TestApiService
import ru.ama.ottest.domain.entity.*
import ru.ama.ottest.domain.repository.TestsRepository
import javax.inject.Inject


class TestsRepositoryImpl @Inject constructor(
    private val mapper: TestMapper,
    private val testQuestionsDao: TestQuestionsDao,
    private val testInfoDao: TestInfoDao,
    private val apiService: TestApiService,
    private val application: Application
) : TestsRepository {
	


    override fun getQuestionsInfoList(testId: Int,limit:Int): List<TestQuestion>{
      ///  var rl:MutableList<TestQuestion> = mutableListOf<TestQuestion>()
        val list=testQuestionsDao.getQuestionListByTestId(testId,limit)
     ///   Log.e("getQuestionsrl1","${testId} ${limit} ${list.toString()}")
		val llist2=list.map {
            mapper.mapDbModelToEntity(it)
        }

     ///   for (l in list)
     ///   {
      ///      rl.add(mapper.mapDbModelToEntity(l))
      ///  }
      ///  Log.e("getQuestionsrl","${testId} ${limit} ${rl.toString()}")
		
		
		
        return llist2

    }
	
	   override fun getAllQuestionsListByTestId(testId: Int): List<TestQuestion>{
        val list=testQuestionsDao.getQuestionListByTestIdAnswers(testId)
		val llist2=list.map {
            mapper.mapDbModelToEntity(it)
        }		
        return llist2
    }
	
	
   /*  fun getQuestionsInfoList2(): LiveData<List<TestQuestion>> {
        return Transformations.map(testQuestionsDao.getQuestionList()) {
            it.map {
                mapper.mapDbModelToEntity(it)
            }
        }
    }*/

    override fun getTestInfo(): List<TestInfo> {
      /*  Log.e("getTestInfo1",testInfoDao.toString())
        Log.e("getTestInfo",testInfoDao.getTestInfo().toString())
       // if(testInfoDao.getTestInfo().value!=null)
        var rl:MutableList<TestInfo> = mutableListOf<TestInfo>()
        for (l in testInfoDao.getTestInfo())
        {
            rl.add(mapper.mapDataDbModelToEntity(l))
        }*/

			val rl=(testInfoDao.getTestInfo()).map  {mapper.mapDataDbModelToEntity(it)}
		
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
                    for ( testDto in testDtoList)
                    {
                        val getJson = apiService.getTestById(testDto.testId.toString())
                        if (!getJson.error) {
                            val questionsDtoList = getJson.questions
                            val dbModelList = questionsDtoList.map {
                                mapper.mapDtoToDbModel(
                                    it,
                                    testDto.testId.toString()
                                )
                            }
                            val questionsItemsCount=testQuestionsDao.insertQuestionList(dbModelList)
                            listOfItems.add(questionsItemsCount.size)
                            Log.e("insertQuestionList",dbModelList.toString())
                        }
                    }


					 
				 }
                
              } catch (e: Exception) {}
			  return listOfItems
		
 
     
    }



}
