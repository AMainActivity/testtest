package ru.ama.ottest.domain.usecase

import ru.ama.ottest.domain.repository.GameRepository
import javax.inject.Inject

class GetTestInfoUseCase @Inject constructor(
    private val repository: GameRepository
) {

    operator fun invoke(testId:Int) = repository.getTestInfo(testId)
}
