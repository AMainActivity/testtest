package ru.ama.ottest.domain.usecase

import ru.ama.ottest.domain.entity.GameSettings
import ru.ama.ottest.domain.entity.Level
import ru.ama.ottest.domain.repository.GameRepository

class GetGameSettingsUseCase(
    private val gameRepository: GameRepository
) {

    operator fun invoke(level: Level): GameSettings = gameRepository.getGameSettings(level)
}
