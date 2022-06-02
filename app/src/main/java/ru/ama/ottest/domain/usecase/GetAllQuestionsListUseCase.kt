package ru.ama.ottest.domain.usecase

import ru.ama.ottest.domain.repository.TestsRepository
import javax.inject.Inject

class GetAllQuestionsListUseCase @Inject constructor(
    private val repository: TestsRepository
) {

    operator fun invoke(testId:Int) = repository.getAllQuestionsListByTestId(testId)
}
