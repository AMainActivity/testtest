package ru.ama.ottest.domain.usecase

import ru.ama.ottest.domain.repository.GameRepository
import javax.inject.Inject

class LoadDataUseCase @Inject constructor(
    private val repository: GameRepository
) {

    operator suspend fun invoke() = repository.loadData()
}
