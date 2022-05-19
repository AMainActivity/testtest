package ru.ama.ottest.presentation

import ru.ama.ottest.domain.entity.GameResult
import ru.ama.ottest.domain.entity.TestQuestion


sealed class State

object ReadyStart : State()
class CurrentNoOfQuestion(val value: Int) : State()
class LeftFormattedTime(val value: String) : State()
class GameResultState(val value: GameResult) : State()
class QuestionState(val value: TestQuestion) : State()
