package ru.ama.ottest.domain.usecase

import ru.ama.ottest.domain.repository.TestsRepository
import javax.inject.Inject

class GetTestInfoUseCase @Inject constructor(
    private val repository: TestsRepository
) {

    operator fun invoke() = repository.getTestInfo()
}
