package ru.ama.ottest.data.mapper

import ru.ama.ottest.data.database.TestInfoDbModel
import ru.ama.ottest.data.database.QuestionDbModel
import ru.ama.ottest.data.network.model.TestInfoDto
import ru.ama.ottest.data.network.model.QuestionDto
import ru.ama.ottest.domain.entity.TestInfoDomModel
import ru.ama.ottest.domain.entity.QuestionDomModel
import javax.inject.Inject

class TestMapper @Inject constructor() {

    fun mapDtoToDbModel(dto: QuestionDto, test_id: String) = QuestionDbModel(
        number = dto.number,
        question = dto.question,
        imageUrl = if (dto.imageUrl?.length!! > 0) BASE_IMAGE_URL + dto.imageUrl else dto.imageUrl,
        answers = dto.answers,
        correct = dto.correct,
        ownerTestId = test_id.toInt()
    )

    fun mapDbModelToEntity(dbModel: QuestionDbModel) = QuestionDomModel(
        number = dbModel.number,
        question = dbModel.question,
        imageUrl = dbModel.imageUrl,
        answers = dbModel.answers,
        correct = dbModel.correct
    )

    fun mapDataDtoToDbModel(dto: TestInfoDto) = TestInfoDbModel(
        testId = dto.testId,
        title = dto.title,
        mainImageUrl = if (dto.mainImageUrl?.length!! > 0) BASE_IMAGE_URL + dto.mainImageUrl else dto.mainImageUrl,
        minPercentOfRightAnswers = dto.minPercentOfRightAnswers,
        testTimeInSeconds = dto.testTimeInSeconds,
        countOfQuestions = dto.countOfQuestions
    )

    fun mapDataDbModelToEntity(dbModel: TestInfoDbModel) = TestInfoDomModel(
        testId = dbModel.testId,
        title = dbModel.title,
        mainImageUrl = dbModel.mainImageUrl,
        minPercentOfRightAnswers = dbModel.minPercentOfRightAnswers,
        testTimeInSeconds = dbModel.testTimeInSeconds,
        countOfQuestions = dbModel.countOfQuestions
    )

    companion object {
        const val BASE_IMAGE_URL = "https://kol.hhos.ru/test/tests/img/"
    }
}
