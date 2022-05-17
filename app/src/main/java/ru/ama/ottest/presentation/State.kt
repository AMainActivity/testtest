package ru.ama.ottest.presentation


sealed class State

object ReadyStart : State()
class CurrentNoOfQuestion(val value: Int) : State()
class LeftFormattedTime(val value: String) : State()
