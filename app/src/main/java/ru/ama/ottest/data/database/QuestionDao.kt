package ru.ama.ottest.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuestionDao {
    @Query("SELECT * FROM test_questions where number in (select number from test_questions order by random() limit 20) ORDER BY number asc")
    fun getQuestionList(): LiveData<List<QuestionDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestionList(priceList: List<QuestionDbModel>): List<Long>

    @Query("SELECT * FROM test_questions where ownerTestId=:testId and number in (select number from test_questions  where ownerTestId=:testId order by random() limit :limit) ORDER BY number asc")
    fun getQuestionListByTestId(testId: Int, limit: Int): List<QuestionDbModel>

    @Query("SELECT * FROM test_questions where ownerTestId=:testId ORDER BY number asc")
    fun getQuestionListByTestIdAnswers(testId: Int): List<QuestionDbModel>
}

