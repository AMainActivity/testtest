package ru.ama.ottest.domain.usecase

import ru.ama.ottest.domain.entity.Questions
import ru.ama.ottest.domain.repository.GameRepository
import javax.inject.Inject

class GenerateQuestionUseCase  @Inject constructor(
    private val gameRepository: GameRepository
) {

    operator fun invoke(questionNo:Int): Questions = gameRepository.generateQuestion(questionNo)
}
