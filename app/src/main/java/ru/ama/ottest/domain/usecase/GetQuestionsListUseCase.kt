package ru.ama.ottest.domain.usecase

import ru.ama.ottest.domain.repository.TestRepository
import javax.inject.Inject

class GetQuestionsListUseCase @Inject constructor(
    private val repository: TestRepository
) {

    operator fun invoke(testId: Int, limit: Int) = repository.getQuestionsInfoList(testId, limit)
}
