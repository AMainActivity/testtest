package ru.ama.ottest.domain.usecase

import ru.ama.ottest.domain.entity.MainTest
import ru.ama.ottest.domain.entity.Questions
import ru.ama.ottest.domain.repository.GameRepository
import javax.inject.Inject

class GetTestInfo  @Inject constructor(private val gameRepository: GameRepository
) {

    operator fun invoke(): MainTest = gameRepository.getTestInfo()
}