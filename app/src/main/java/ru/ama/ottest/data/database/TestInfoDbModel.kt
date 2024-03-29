package ru.ama.ottest.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.ama.ottest.data.database.TestInfoDbModel.Companion.tabTestInfo

@Entity(tableName = tabTestInfo)
data class TestInfoDbModel(
    @PrimaryKey
    val testId: Int,
    val title: String,
    val mainImageUrl: String? = null,
    val minPercentOfRightAnswers: Int,
    val testTimeInSeconds: Int,
    val countOfQuestions: Int
) {


    companion object {
        const val tabTestInfo = "test_info"
    }
}