package ru.ama.ottest.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName

@Entity(
  tableName = "test_questions",
  foreignKeys = [
    ForeignKey(entity = TestInfoDbModel::class, parentColumns = ["testId"], childColumns = ["ownerTestId"],
      onDelete = ForeignKey.CASCADE)
  ]
)
data class TestQuestionsDbModel(
  @PrimaryKey
  var number: Int,
  var question: String,
  var imageUrl : String? = null,
  var answers: List<String>,
  var correct: List<Int>,
  var ownerTestId: Int
)
