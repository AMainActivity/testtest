package ru.ama.ottest.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TestInfoDao {
   /* @Query("SELECT * FROM test_info ORDER BY testId asc")
    fun getTestInfo(): LiveData<List<TestInfoDbModel>>*/

@Query("SELECT * FROM test_info where testId= :testId ORDER BY testId asc limit 1")
    fun getTestInfo(testId:Int): TestInfoDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestInfo(testInfo: TestInfoDbModel)
}