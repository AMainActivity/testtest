package ru.ama.ottest.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.ama.ottest.data.network.model.TestQuestionsDto
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.ama.ottest.data.database.TestInfoDbModel.Companion.tabTestInfo

@Entity(tableName = tabTestInfo)
data class TestInfoDbModel(
  @PrimaryKey
  val testId: Int,
  val title: String,
  val mainImageUrl : String? = null,
  val minPercentOfRightAnswers: Int,
  val testTimeInSeconds: Int,
  val countOfQuestions: Int
)
{
	
	
	    companion object {
        const val tabTestInfo = "test_info"
		}
}

/*
class TestAndQuestions {
   @Embedded
   var test: TestInfoDbModel? = null
   @Relation(parentColumn = “testId”,
             entityColumn = “ownerTestId”)
   var questions: List<TestQuestionsDbModel> = ArrayList()
}
*/