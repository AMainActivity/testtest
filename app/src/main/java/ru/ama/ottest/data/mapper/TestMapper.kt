package ru.ama.ottest.data.mapper

import ru.ama.ottest.data.database.TestInfoDbModel
import ru.ama.ottest.data.database.TestQuestionsDbModel
import ru.ama.ottest.data.network.model.TestDataDto
import ru.ama.ottest.data.network.model.TestListDataDto
import ru.ama.ottest.data.network.model.TestQuestionsDto
import ru.ama.ottest.domain.entity.TestInfo
import ru.ama.ottest.domain.entity.TestQuestion
import javax.inject.Inject

class TestMapper @Inject constructor() {

    fun mapDtoToDbModel(dto: TestQuestionsDto, test_id: String) = TestQuestionsDbModel(
        number = dto.number,
        question = dto.question,
        imageUrl  = dto.imageUrl,
        answers = dto.answers,
        correct =  dto.correct,
        ownerTestId = test_id.toInt()
    )

    fun mapDbModelToEntity(dbModel: TestQuestionsDbModel) = TestQuestion(
        number = dbModel.number,
        question = dbModel.question,
        imageUrl  = dbModel.imageUrl,
        answers = dbModel.answers,
        correct =  dbModel.correct
    )
    fun mapDataDtoToDbModel(dto: TestListDataDto) = TestInfoDbModel(
         testId=dto.testId,
     title=dto.title,
     mainImageUrl=dto.mainImageUrl,
     minCountOfRightAnswers=dto.minCountOfRightAnswers,
     minPercentOfRightAnswers=dto.minPercentOfRightAnswers,
     testTimeInSeconds=dto.testTimeInSeconds,
     countOfQuestions=dto.countOfQuestions
    )
    fun mapDataDbModelToEntity(dbModel: TestInfoDbModel) = TestInfo(
        testId=dbModel.testId,
        title=dbModel.title,
        mainImageUrl=dbModel.mainImageUrl,
        minCountOfRightAnswers=dbModel.minCountOfRightAnswers,
        minPercentOfRightAnswers=dbModel.minPercentOfRightAnswers,
        testTimeInSeconds=dbModel.testTimeInSeconds,
        countOfQuestions=dbModel.countOfQuestions
    )
/*
fun mapJsonContainerToListCoinInfo(jsonContainer: CoinInfoJsonContainerDto): List<CoinInfoDto> {
        val result = mutableListOf<CoinInfoDto>()
        val jsonObject = jsonContainer.json ?: return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinInfoDto::class.java
                )
                result.add(priceInfo)
            }
        }
        return result
    }
*/
    companion object {

    }
}
