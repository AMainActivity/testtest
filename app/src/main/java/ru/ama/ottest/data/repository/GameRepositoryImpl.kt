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
import ru.ama.ottest.data.workers.TestRefreshDataWorker
import ru.ama.ottest.domain.entity.*
import ru.ama.ottest.domain.repository.GameRepository
import javax.inject.Inject


class GameRepositoryImpl @Inject constructor(
    private val mapper: TestMapper,
    private val testQuestionsDao: TestQuestionsDao,
    private val testInfoDao: TestInfoDao,
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

    override fun loadData() {
        val workManager = WorkManager.getInstance(application)
                //ListenableFuture<List<WorkInfo>> statuses = instance.getWorkInfosByTag(tag);
         //  val statuses :ListenableFuture<List<WorkInfo>> = workManager.getWorkInfosByTag(TestRefreshDataWorker.NAME)

        workManager.enqueueUniqueWork(
            TestRefreshDataWorker.NAME,
            ExistingWorkPolicy.REPLACE,
            TestRefreshDataWorker.makeRequest()
        )
       /* val workInfo = workManager.getWorkInfoById(TestRefreshDataWorker.makeRequest().id).get()
        val wasSuccess = workInfo.outputData.getBoolean("is_success", false)
        Log.e("wasSuccess",wasSuccess.toString())*/
      /*  WorkManager.getInstance(application)
            // requestId is the WorkRequest id
            .getWorkInfoByIdLiveData(TestRefreshDataWorker.makeRequest().id)
            .observe(observer, Observer { workInfo: WorkInfo? ->
                if (workInfo != null) {
                    val progress = workInfo.progress
                    val value = progress.getInt(Progress, 0)
                    // Do something with progress information
                }
            })*/
        /*workManager.getWorkInfoById(TestRefreshDataWorker.makeRequest().id)
            .observe(application, Observer { info ->
                if (info != null && info.state.isFinished) {
                    val myResult = info.outputData.getBoolean("is_success",
                        false)
                    // ... do something with the result ...
                }
            })*/
    }



}
