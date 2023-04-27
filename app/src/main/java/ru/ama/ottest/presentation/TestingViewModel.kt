package ru.ama.ottest.presentation

import android.os.CountDownTimer
import androidx.lifecycle.*
import kotlinx.coroutines.*
import ru.ama.ottest.domain.entity.*
import ru.ama.ottest.domain.usecase.*
import javax.inject.Inject

class TestingViewModel @Inject constructor(
    private val getQuestionsListUseCase: GetQuestionsListUseCase
) : ViewModel() {

    private var currentNoOfQuestion: Int = PARAMETER__MINUS_ODIN
    private var timer: CountDownTimer? = null
    private var countOfRightAnswers = PARAMETER_ZERO
    private var countOfWrongAnswers = PARAMETER_ZERO
    private var listUserAnswerDomModels: MutableList<UserAnswerDomModel> = mutableListOf()

    private val _readyStart = MutableLiveData<Unit>()
    val readyStart: LiveData<Unit>
        get() = _readyStart

    lateinit var testInfoDomModel: TestInfoDomModel
    lateinit var questionDomModel: List<QuestionDomModel>

    private val _question = MutableLiveData<QuestionDomModel>()
    val question: LiveData<QuestionDomModel>
        get() = _question

    private val _leftFormattedTime = MutableLiveData<String>()
    val leftFormattedTime: LiveData<String>
        get() = _leftFormattedTime

    private val _curNumOfQuestion = MutableLiveData<Int>()
    val curNumOfQuestion: LiveData<Int>
        get() = _curNumOfQuestion

    private val _gameResult = MutableLiveData<TestResultDomModel>()
    val gameResult: LiveData<TestResultDomModel>
        get() = _gameResult

    private var millisAfterStart = PARAMETER_ZERO_LONG

    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    private val _percentOfRightAnswersStr = MutableLiveData<String>()
    val percentOfRightAnswersStr: LiveData<String>
        get() = _percentOfRightAnswersStr

    val enoughPercentage: LiveData<Boolean> = Transformations.map(percentOfRightAnswers) {
        it >= testInfoDomModel.minPercentOfRightAnswers
    }

    init {
        currentNoOfQuestion = PARAMETER_ZERO
    }

    private fun getTInfo() {

        val d = viewModelScope.async(Dispatchers.IO) {
            getQuestionsListUseCase(testInfoDomModel.testId, testInfoDomModel.countOfQuestions)

        }
        viewModelScope.launch {
            questionDomModel = d.await()
            _readyStart.postValue(Unit)
        }
    }

    fun setParams(tInfo: TestInfoDomModel) {
        testInfoDomModel = tInfo
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
		val b=(answer.containsAll(rightAnswer!!.correct)&& rightAnswer!!.correct.containsAll(answer))
        if (b/*answer == rightAnswer!!.correct*/)
        {
            countOfRightAnswers++
        } else {
            countOfWrongAnswers++
            val rOt = UserAnswerDomModel(
                currentNoOfQuestion + 1,
                rightAnswer.question,
                rightAnswer.imageUrl,
                rightAnswer.answers,
                answer,
                rightAnswer.correct
            )
            listUserAnswerDomModels.add(rOt)
        }
    }

    private fun randomElementsFromAnswersList(list: List<String>, randCount: Int): List<String> {
        return list.asSequence().shuffled().take(randCount).toList()
    }

    private fun startTimer() {
        millisAfterStart = PARAMETER_ZERO_LONG
        timer = object : CountDownTimer(
            testInfoDomModel.testTimeInSeconds * MILLIS_IN_SECONDS + MILLIS_IN_SECONDS,
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
        if (questionNo < testInfoDomModel.countOfQuestions) {
            val oldAnswerList = questionDomModel[questionNo].answers
            val oldCorrectAnswers = questionDomModel[questionNo].correct
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
            val newQuestion = questionDomModel[questionNo].copy(
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

    private fun getGameResult(): TestResultDomModel {
        val percentOfRightAnswers = getPercentOfRightAnswers()
        val enoughPercentage = percentOfRightAnswers >= testInfoDomModel.minPercentOfRightAnswers
        val winner = enoughPercentage
        val countOfQuestions = countOfRightAnswers + countOfWrongAnswers
        return TestResultDomModel(
            testInfoDomModel.title,
            getFormattedLeftTime(testInfoDomModel.testTimeInSeconds * MILLIS_IN_SECONDS + MILLIS_IN_SECONDS - millisAfterStart),
            currentNoOfQuestion,
            winner,
            countOfRightAnswers,
            countOfQuestions,
            testInfoDomModel.minPercentOfRightAnswers,
            testInfoDomModel.testTimeInSeconds,
            listUserAnswerDomModels
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
            "$percentOfRightAnswers/${testInfoDomModel.minPercentOfRightAnswers}"
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
