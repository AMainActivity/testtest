package ru.ama.ottest.data.repository

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.google.gson.Gson
import ru.ama.ottest.data.database.TestInfoDao
import ru.ama.ottest.data.database.TestQuestionsDao
import ru.ama.ottest.data.mapper.TestMapper
import ru.ama.ottest.data.workers.TestRefreshDataWorker
import ru.ama.ottest.domain.entity.*
import ru.ama.ottest.domain.repository.GameRepository
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class GameRepositoryImpl @Inject constructor(
    private val mapper: TestMapper,
    private val testQuestionsDao: TestQuestionsDao,
    private val testInfoDao: TestInfoDao,
    private val application: Application
) : GameRepository {
	
	val mainTest by lazy{ getTestInfo()}
    val questionsAll by lazy { getQuestionsInfoList() }
    lateinit var questionsForTest: List<TestQuestion>
	/*by lazy {
		 randomElementsFromQuestionsList(questionsAll,mainTest.countOfQuestions)
    }*/
    /*override fun generateQuestion1(
        maxValue: Int,
        minSumValue: Int,
        minAnswerValue: Int,
        countOfOptions: Int
    ): Question {
        val sum = Random.nextInt(minSumValue, maxValue + 1)
        val visibleNumber = Random.nextInt(minAnswerValue, sum)
        val options = HashSet<Int>()
        val rightAnswer = sum - visibleNumber
        options.add(rightAnswer)
        val from = max(minAnswerValue, rightAnswer - countOfOptions)
        val to = min(rightAnswer + countOfOptions, maxValue)
        while (options.size < countOfOptions) {
            options.add(Random.nextInt(from, to + 1))
        }
        return Question(sum, visibleNumber, options.toList())
    }  */

    override fun getQuestionsInfoList(): LiveData<List<TestQuestion>> {
        return Transformations.map(testQuestionsDao.getQuestionList()) {
            it.map {
                mapper.mapDbModelToEntity(it)
            }
        }
    }

    override fun getTestInfo(): LiveData<List<TestInfo>> {
        Log.e("getTestInfo",testInfoDao.getTestInfo().value.toString())
       // if(testInfoDao.getTestInfo().value!=null)
        return  Transformations.map(testInfoDao.getTestInfo()) {
            it.map {  mapper.mapDataDbModelToEntity(it)}
        }
    }

    override fun loadData() {
        val workManager = WorkManager.getInstance(application)
        workManager.enqueueUniqueWork(
            TestRefreshDataWorker.NAME,
            ExistingWorkPolicy.REPLACE,
            TestRefreshDataWorker.makeRequest()
        )
    }





	/*override fun getTestInfo(): MainTest {
		return mainTest
	}*/


override fun shuffleListOfQuestions()
{
	questionsForTest = randomElementsFromQuestionsList(questionsAll.value!!,mainTest.value!![0].countOfQuestions)
}

private fun randomElementsFromQuestionsList(list: List<TestQuestion>, randCount:Int):List<TestQuestion> {
    return list.asSequence().shuffled().take(randCount).toList()
}


	override fun generateQuestion(questionNo:Int): Questions {
	

	val number=questionsAll.value!![questionNo].number
	val question=questionsAll.value!![questionNo].question
	val imageUrl=questionsAll.value!![questionNo].imageUrl
	val answers=questionsAll.value!![questionNo].answers
	val correct=questionsAll.value!![questionNo].correct
	
	return Questions(number, question, imageUrl,answers,correct)
    }  
	
	

    override fun getGameSettings(): GameSettings {
	
        return GameSettings(
                mainTest.value!![0].minCountOfRightAnswers,
				mainTest.value!![0].minPercentOfRightAnswers,
				mainTest.value!![0].testTimeInSeconds
            )
    }
	
}
