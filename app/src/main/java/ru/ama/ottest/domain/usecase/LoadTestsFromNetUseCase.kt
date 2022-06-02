package ru.ama.ottest.domain.usecase

import ru.ama.ottest.domain.repository.TestsRepository
import javax.inject.Inject

class LoadTestsFromNetUseCase @Inject constructor(
    private val repository: TestsRepository
) {

    operator suspend fun invoke() = repository.loadTestsFromNet()
}
