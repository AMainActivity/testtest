package ru.ama.ottest.presentation

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.ama.ottest.data.GameRepositoryImpl
import ru.ama.ottest.domain.entity.GameResult
import ru.ama.ottest.domain.entity.GameSettings
import ru.ama.ottest.domain.entity.MainTest
import ru.ama.ottest.domain.entity.Questions
import ru.ama.ottest.domain.usecase.GenerateQuestionUseCase
import ru.ama.ottest.domain.usecase.GetCurrentNoOfQuestion
import ru.ama.ottest.domain.usecase.GetGameSettingsUseCase
import ru.ama.ottest.domain.usecase.GetTestInfo

class GameViewModel : ViewModel() {

    private val repository = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)
    private val getTestInfoUserCase = GetTestInfo(repository)
    private val getCurrentNoOfQuestionUserCase = GetCurrentNoOfQuestion(repository)

    private lateinit var gameSettings: GameSettings
    private lateinit var testInfo: MainTest
    private var currentNoOfQuestion: Int=-1

    private val _minPercentOfRightAnswers = MutableLiveData<Int>()
    val minPercentOfRightAnswers: LiveData<Int>
        get() = _minPercentOfRightAnswers

    private val _leftFormattedTime = MutableLiveData<String>()
    val leftFormattedTime: LiveData<String>
        get() = _leftFormattedTime

    private val _question = MutableLiveData<Questions>()
    val question: LiveData<Questions>
        get() = _question

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    val enoughPercentage: LiveData<Boolean> = Transformations.map(percentOfRightAnswers) {
        it >= gameSettings.minPercentOfRightAnswers
    }

    private var timer: CountDownTimer? = null
    private var countOfRightAnswers = 0
    private var countOfWrongAnswers = 0

    fun startGame() {
        setupGameSettings()
        startTimer()
        generateQuestion()
        currentNoOfQuestion=0
    }

    fun chooseAnswer(answer: Int) {
        if (gameResult.value != null) {
            return
        }
        checkAnswer(answer)
        getPercentOfRightAnswers()
        generateQuestion()
        currentNoOfQuestion=getCurrentNoOfQuestionUserCase()
    }

    private fun setupGameSettings() {
        gameSettings = getGameSettingsUseCase()
        testInfo=getTestInfoUserCase()
        _minPercentOfRightAnswers.value = gameSettings.minPercentOfRightAnswers
    }

    private fun checkAnswer(answer: Int) {
        val rightAnswer = question.value
        if (answer == rightAnswer!!.correct[0]) {
            countOfRightAnswers++
        } else {
            countOfWrongAnswers++
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSettings.testTimeInSeconds * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _leftFormattedTime.value = getFormattedLeftTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }

    private fun generateQuestion() {
        Log.e("generateQuestion","cur: $currentNoOfQuestion, size: ${testInfo.questions.size-1}")
        if (currentNoOfQuestion<testInfo.questions.size-1)
        _question.value = generateQuestionUseCase()
        else
            finishGame()
    }

    private fun finishGame() {
        _leftFormattedTime.value = getFormattedLeftTime(0)
        _gameResult.value = getGameResult()
    }

    private fun getGameResult(): GameResult {
        val percentOfRightAnswers = getPercentOfRightAnswers()
        val enoughPercentage = percentOfRightAnswers > gameSettings.minPercentOfRightAnswers
        val enoughRightAnswers = countOfRightAnswers >= gameSettings.minCountOfRightAnswers
        val winner = enoughPercentage && enoughRightAnswers
        val countOfQuestions = countOfRightAnswers + countOfWrongAnswers
        return GameResult(winner, countOfRightAnswers, countOfQuestions, gameSettings)
    }

    private fun getPercentOfRightAnswers(): Int {
        val countOfQuestions = countOfRightAnswers + countOfWrongAnswers
        val percentOfRightAnswers = if (countOfQuestions > 0) {
            ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
        } else {
            0
        }
        _percentOfRightAnswers.value = percentOfRightAnswers
        return percentOfRightAnswers
    }

    private fun getFormattedLeftTime(millisUntilFinished: Long): String {
        val seconds = (millisUntilFinished / MILLIS_IN_SECONDS).toInt()
        val minutes = seconds / SECONDS_IN_MINUTE
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {

        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTE = 60
    }
}
