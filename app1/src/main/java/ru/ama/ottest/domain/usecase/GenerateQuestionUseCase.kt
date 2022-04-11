package ru.ama.ottest.domain.usecase

import ru.ama.ottest.domain.entity.Questions
import ru.ama.ottest.domain.repository.GameRepository

class GenerateQuestionUseCase(
    private val gameRepository: GameRepository
) {

    operator fun invoke(questionNo:Int): Questions = gameRepository.generateQuestion(questionNo)
}
