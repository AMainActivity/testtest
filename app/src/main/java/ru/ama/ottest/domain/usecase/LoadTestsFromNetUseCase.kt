package ru.ama.ottest.domain.usecase

import ru.ama.ottest.domain.repository.TestRepository
import javax.inject.Inject

class LoadTestsFromNetUseCase @Inject constructor(
    private val repository: TestRepository
) {

    suspend operator fun invoke() = repository.loadTestsFromNet()
}
