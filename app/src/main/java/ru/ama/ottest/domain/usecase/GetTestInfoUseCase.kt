package ru.ama.ottest.domain.usecase

import ru.ama.ottest.domain.repository.TestRepository
import javax.inject.Inject

class GetTestInfoUseCase @Inject constructor(
    private val repository: TestRepository
) {

    operator fun invoke() = repository.getTestInfo()
}
