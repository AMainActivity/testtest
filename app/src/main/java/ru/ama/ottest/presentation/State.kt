package ru.ama.ottest.presentation


sealed class State
{
    object sss : State()}

object ReadyStart : State()
//class CurrentNoOfQuestion(val value: Int) : State()
class MinPercentOfRightAnswers(val value: Int) : State()
//class PercentOfRightAnswers(val value: Int) : State()
class LeftFormattedTime(val value: String) : State()
//class Question(val value: ru.ama.ottest.domain.entity.TestQuestion) : State()
//class GameResult2(val value: ru.ama.ottest.domain.entity.GameResult) : State()