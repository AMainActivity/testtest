package ru.ama.ottest.domain.usecase

import ru.ama.ottest.domain.repository.TestsRepository
import javax.inject.Inject

class LoadDataUseCase @Inject constructor(
    private val repository: TestsRepository
) {

    operator suspend fun invoke() = repository.loadData()
}
