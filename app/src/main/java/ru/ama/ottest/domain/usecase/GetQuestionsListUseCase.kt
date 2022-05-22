package ru.ama.ottest.domain.usecase

import ru.ama.ottest.domain.repository.TestsRepository
import javax.inject.Inject

class GetQuestionsListUseCase @Inject constructor(
    private val repository: TestsRepository
) {

    operator fun invoke(testId:Int,limit:Int) = repository.getQuestionsInfoList(testId,limit)
}
