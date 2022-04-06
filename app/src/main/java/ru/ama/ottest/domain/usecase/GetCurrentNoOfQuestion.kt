package ru.ama.ottest.domain.usecase

import ru.ama.ottest.domain.entity.MainTest
import ru.ama.ottest.domain.repository.GameRepository

class GetCurrentNoOfQuestion (  private val gameRepository: GameRepository
) {

    operator fun invoke(): Int = gameRepository.getCurrentNoOfQuestion()
}