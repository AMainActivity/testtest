package ru.ama.ottest.data

import ru.ama.ottest.domain.repository.GameRepository
import ru.ama.ottest.domain.entity.GameSettings
import ru.ama.ottest.domain.entity.Level
import ru.ama.ottest.domain.entity.Question
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object GameRepositoryImpl : GameRepository {

    override fun generateQuestion(
        maxValue: Int,
        minSumValue: Int,
        minAnswerValue: Int,
        countOfOptions: Int
    ): Question {
        val sum = Random.nextInt(minSumValue, maxValue + 1)
        val visibleNumber = Random.nextInt(minAnswerValue, sum)
        val options = HashSet<Int>()
        val rightAnswer = sum - visibleNumber
        options.add(rightAnswer)
        val from = max(minAnswerValue, rightAnswer - countOfOptions)
        val to = min(rightAnswer + countOfOptions, maxValue)
        while (options.size < countOfOptions) {
            options.add(Random.nextInt(from, to + 1))
        }
        return Question(sum, visibleNumber, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when (level) {
            Level.EASY   -> GameSettings(
                10,
                20,
                80,
                60
            )
            Level.NORMAL -> GameSettings(
                20,
                20,
                85,
                40
            )
            Level.HARD   -> GameSettings(
                30,
                20,
                90,
                30
            )
            Level.TEST   -> GameSettings(
                10,
                3,
                50,
                8
            )
        }
    }
}
