package ru.ama.ottest.presentation

import android.content.Context
import android.os.CountDownTimer
import android.widget.ArrayAdapter
import androidx.lifecycle.*
import kotlinx.coroutines.*
import ru.ama.ottest.domain.entity.*
import ru.ama.ottest.domain.usecase.*
import javax.inject.Inject

class ViewModelTestProcess @Inject constructor(
    private val getQuestionsListUseCase: GetQuestionsListUseCase
) : ViewModel() {

    private var currentNoOfQuestion: Int = PARAMETER__MINUS_ODIN
    private var timer: CountDownTimer? = null
    private var countOfRightAnswers = PARAMETER_ZERO
    private var countOfWrongAnswers = PARAMETER_ZERO
    private var listAnswerOfTests: MutableList<AnswerOfTest> = mutableListOf()

    private val _readyStart = MutableLiveData<Unit>()
    val readyStart: LiveData<Unit>
        get() = _readyStart

    lateinit var testInfo: TestInfo
    lateinit var testQuestion: List<TestQuestion>

    private val _question = MutableLiveData<TestQuestion>()
    val question: LiveData<TestQuestion>
        get() = _question

    private val _leftFormattedTime = MutableLiveData<String>()
    val leftFormattedTime: LiveData<String>
        get() = _leftFormattedTime

    private val _curNumOfQuestion = MutableLiveData<Int>()
    val curNumOfQuestion: LiveData<Int>
        get() = _curNumOfQuestion

    private val _gameResult = MutableLiveData<TestsResult>()
    val gameResult: LiveData<TestsResult>
        get() = _gameResult

    private var millisAfterStart = PARAMETER_ZERO_LONG

    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    private val _percentOfRightAnswersStr = MutableLiveData<String>()
    val percentOfRightAnswersStr: LiveData<String>
        get() = _percentOfRightAnswersStr

    val enoughPercentage: LiveData<Boolean> = Transformations.map(percentOfRightAnswers) {
        it >= testInfo.minPercentOfRightAnswers
    }

    init {
        currentNoOfQuestion = PARAMETER_ZERO
    }

    private fun getTInfo() {

        val d = viewModelScope.async(Dispatchers.IO) {
            getQuestionsListUseCase(testInfo.testId, testInfo.countOfQuestions)

        }
        viewModelScope.launch {
            testQuestion = d.await()
            _readyStart.postValue(Unit)
        }
    }

    fun setParams(tInfo: TestInfo) {
        testInfo = tInfo
        getTInfo()
    }



    fun startGame() {
        startTimer()
        _curNumOfQuestion.value = currentNoOfQuestion
        generateQuestion(currentNoOfQuestion)
    }

    fun chooseAnswer(answer: List<Int>) {
        if (gameResult.value != null) {
            return
        }
        checkAnswer(answer)
        getPercentOfRightAnswers()
        currentNoOfQuestion++
        _curNumOfQuestion.value = currentNoOfQuestion
        generateQuestion(currentNoOfQuestion)
    }


    private fun checkAnswer(answer: List<Int>/*массив*/) {
        val rightAnswer = question.value
		//сравнить массивы
        if (answer == rightAnswer!!.correct)
        {
            countOfRightAnswers++
        } else {
            countOfWrongAnswers++
            val rOt = AnswerOfTest(
                currentNoOfQuestion + 1,
                rightAnswer.question,
                rightAnswer.imageUrl,
                rightAnswer.answers,
                answer,
                rightAnswer.correct
            )
            listAnswerOfTests.add(rOt)
        }
    }

    private fun randomElementsFromAnswersList(list: List<String>, randCount: Int): List<String> {
        return list.asSequence().shuffled().take(randCount).toList()
    }

    private fun startTimer() {
        millisAfterStart = PARAMETER_ZERO_LONG
        timer = object : CountDownTimer(
            testInfo.testTimeInSeconds * MILLIS_IN_SECONDS + MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                millisAfterStart = millisUntilFinished
                _leftFormattedTime.value = getFormattedLeftTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }

    private fun generateQuestion(questionNo: Int) {
        if (questionNo < testInfo.countOfQuestions) {
            val oldAnswerList = testQuestion[questionNo].answers
            val oldCorrectAnswers = testQuestion[questionNo].correct
			val oldCorrectAnswerList: MutableList<String> = mutableListOf()
                for (cor in oldCorrectAnswers) {
					oldCorrectAnswerList.add(oldAnswerList[cor])
				}
			val newCorrectAnswerIndexList: MutableList<Int> = mutableListOf()
            val newAnswerList = randomElementsFromAnswersList(oldAnswerList, oldAnswerList.size)
			for (newCor in oldCorrectAnswerList) {
					newCorrectAnswerIndexList.add(newAnswerList.indexOf(newCor))
				}
			
			
          //  val oldCorrectAnswerString = oldAnswerList[oldCorrectAnswers[PARAMETER_ZERO]]
			
			
			
			
         //   val newCorrectAnswerIndex = newAnswerList.indexOf(oldCorrectAnswerString)
            val newQuestion = testQuestion[questionNo].copy(
                answers = newAnswerList,
                correct =newCorrectAnswerIndexList// listOf(newCorrectAnswerIndex)
            )

            _question.value = newQuestion
        } else
            finishGame()
    }

    private fun finishGame() {
        _leftFormattedTime.value = getFormattedLeftTime(PARAMETER_ZERO_LONG)
        _gameResult.value = getGameResult()
    }

    private fun getGameResult(): TestsResult {
        val percentOfRightAnswers = getPercentOfRightAnswers()
        val enoughPercentage = percentOfRightAnswers >= testInfo.minPercentOfRightAnswers
        val winner = enoughPercentage
        val countOfQuestions = countOfRightAnswers + countOfWrongAnswers
        return TestsResult(
            testInfo.title,
            getFormattedLeftTime(testInfo.testTimeInSeconds * MILLIS_IN_SECONDS + MILLIS_IN_SECONDS - millisAfterStart),
            currentNoOfQuestion,
            winner,
            countOfRightAnswers,
            countOfQuestions,
            testInfo.minPercentOfRightAnswers,
            testInfo.testTimeInSeconds,
            listAnswerOfTests
        )
    }

    private fun getPercentOfRightAnswers(): Int {
        val countOfQuestions = countOfRightAnswers + countOfWrongAnswers
        val percentOfRightAnswers = if (countOfQuestions > PARAMETER_ZERO) {
            ((countOfRightAnswers / countOfQuestions.toDouble()) * PARAMETER_STO).toInt()
        } else {
            PARAMETER_ZERO
        }
        _percentOfRightAnswers.value = percentOfRightAnswers
        _percentOfRightAnswersStr.value =
            "$percentOfRightAnswers/${testInfo.minPercentOfRightAnswers}"
        return percentOfRightAnswers
    }

    private fun getFormattedLeftTime(millisUntilFinished: Long): String {

        val seconds = (millisUntilFinished / MILLIS_IN_SECONDS % SECONDS_IN_MINUTE).toInt()
        val minutes = millisUntilFinished / MILLIS_IN_SECONDS / SECONDS_IN_MINUTE
        return String.format(FORMATTED_STRING_MINUTE_SECOND, minutes, seconds)
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {

        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTE = 60
        private const val PARAMETER_ZERO = 0
        private const val PARAMETER_ZERO_LONG = 0L
        private const val PARAMETER__MINUS_ODIN = -1
        private const val PARAMETER_STO = 100
        private const val FORMATTED_STRING_MINUTE_SECOND = "%02d:%02d"
    }
}
