package ru.ama.ottest.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.ama.ottest.data.network.model.TestQuestionsDto
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "test_info")
data class TestInfoDbModel(
  @PrimaryKey
  var testId: Int,
  var title: String,
  var mainImageUrl : String? = null,
  var minCountOfRightAnswers: Int,
  var minPercentOfRightAnswers: Int,
  var testTimeInSeconds: Int,
  var countOfQuestions: Int
)
