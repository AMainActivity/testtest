package ru.ama.ottest.domain.usecase

import ru.ama.ottest.domain.repository.TestRepository
import javax.inject.Inject

class GetAllQuestionsListUseCase @Inject constructor(
    private val repository: TestRepository
) {

    operator fun invoke(testId: Int) = repository.getAllQuestionsListByTestId(testId)
}
