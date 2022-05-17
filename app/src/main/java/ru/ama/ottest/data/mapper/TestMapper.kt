package ru.ama.ottest.data.mapper

import ru.ama.ottest.data.database.TestInfoDbModel
import ru.ama.ottest.data.database.TestQuestionsDbModel
import ru.ama.ottest.data.network.model.TestListDataDto
import ru.ama.ottest.data.network.model.TestQuestionsDto
import ru.ama.ottest.domain.entity.TestInfo
import ru.ama.ottest.domain.entity.TestQuestion
import javax.inject.Inject

class TestMapper @Inject constructor() {

    fun mapDtoToDbModel(dto: TestQuestionsDto, test_id: String) = TestQuestionsDbModel(
        //_id = dto._id!!,
        number = dto.number,
        question = dto.question,
        imageUrl  = if (dto.imageUrl?.length!!>0)  BASE_IMAGE_URL + dto.imageUrl else dto.imageUrl,
        answers = dto.answers,
        correct =  dto.correct,
        ownerTestId = test_id.toInt()
    )

    fun mapDbModelToEntity(dbModel: TestQuestionsDbModel) = TestQuestion(
        //_id = dbModel._id,
        number = dbModel.number,
        question = dbModel.question,
        imageUrl  = dbModel.imageUrl,
        answers = dbModel.answers,
        correct =  dbModel.correct
    )
    fun mapDataDtoToDbModel(dto: TestListDataDto) = TestInfoDbModel(
         testId=dto.testId,
     title=dto.title,
     mainImageUrl= if (dto.mainImageUrl?.length!!>0)  BASE_IMAGE_URL + dto.mainImageUrl else dto.mainImageUrl,
     minPercentOfRightAnswers=dto.minPercentOfRightAnswers,
     testTimeInSeconds=dto.testTimeInSeconds,
     countOfQuestions=dto.countOfQuestions
    )
    fun mapDataDbModelToEntity(dbModel: TestInfoDbModel) = TestInfo(
        testId=dbModel.testId,
        title=dbModel.title,
        mainImageUrl=dbModel.mainImageUrl,
        minPercentOfRightAnswers=dbModel.minPercentOfRightAnswers,
        testTimeInSeconds=dbModel.testTimeInSeconds,
        countOfQuestions=dbModel.countOfQuestions
    )

    companion object {
        const val BASE_IMAGE_URL = "https://kol.hhos.ru/test/tests/img/"
    }
}
