package ru.ama.ottest.presentation

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*
import ru.ama.ottest.domain.entity.*
import ru.ama.ottest.domain.usecase.*
import javax.inject.Inject

class TestProcessViewModel @Inject constructor(
    private val getQuestionsListUseCase: GetQuestionsListUseCase,
    private val loadDataUseCase: LoadDataUseCase,
    private val getTestInfoUseCase: GetTestInfoUseCase
) : ViewModel() {

    private var currentNoOfQuestion: Int = PARAMETER__MINUS_ODIN

    init {
        currentNoOfQuestion = PARAMETER_ZERO
    }
     private val _readyStart = MutableLiveData<Unit>()
     val readyStart: LiveData<Unit>
         get() = _readyStart

    private fun getTInfo() {
     
        val d=viewModelScope.async(Dispatchers.IO) {
            getQuestionsListUseCase(testInfo.testId, testInfo.countOfQuestions)

        }
        viewModelScope.launch {
            testQuestion = d.await()
                Log.e("getTestInfoUseCase", testInfo.toString())
            Log.e("getQuestionsListUseCase", testQuestion[PARAMETER_ZERO].toString())
            _readyStart.postValue(Unit)
        }
    }


    //val kolvoOfQuestions by lazy { testInfo.countOfQuestions }

    lateinit var testInfo: TestInfo 
    lateinit var testQuestion: List<TestQuestion>

    fun setParams(tInfo: TestInfo) {
        testInfo = tInfo
        getTInfo()
    }

    private val testsSettings: TestsSettings by lazy {
        TestsSettings(
            testInfo.minPercentOfRightAnswers,
            testInfo.testTimeInSeconds
        )
    }

  /*  private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state*/
   
  
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
        it >= testsSettings.minPercentOfRightAnswers
    }

    private var timer: CountDownTimer? = null
    private var countOfRightAnswers = PARAMETER_ZERO
    private var countOfWrongAnswers = PARAMETER_ZERO
    private var listAnswerOfTests: MutableList<AnswerOfTest> = mutableListOf<AnswerOfTest>()
//private lateinit var curQuestin:TestQuestion
    fun startGame() {
        startTimer()
        _curNumOfQuestion.value = currentNoOfQuestion
        generateQuestion(currentNoOfQuestion)
    }

    fun chooseAnswer(answer: Int) {
       /* if (gameResult.value != null) {
            return
        }*/
        checkAnswer(answer)
        getPercentOfRightAnswers()
        currentNoOfQuestion++
        _curNumOfQuestion.value = currentNoOfQuestion
        generateQuestion(currentNoOfQuestion)
    }


    private fun checkAnswer(answer: Int) {
        val rightAnswer = /*curQuestin*/question.value
      //  _rightAnswerLD.value=answer == rightAnswer!!.correct[0]
        if (answer == rightAnswer!!.correct[PARAMETER_ZERO]) {        
            countOfRightAnswers++
        } else {
            countOfWrongAnswers++
            val rOt = AnswerOfTest(
                currentNoOfQuestion+1,
                rightAnswer.question,
                rightAnswer.imageUrl,
                rightAnswer.answers,
                answer,
                rightAnswer.correct[PARAMETER_ZERO]
            )
            listAnswerOfTests.add(rOt)
        }
    }


    private fun randomElementsFromAnswersList(list: List<String>, randCount: Int): List<String> {
        return list.asSequence().shuffled().take(randCount).toList()
    }

    private fun startTimer() {
		millisAfterStart=PARAMETER_ZERO_LONG
        timer = object : CountDownTimer(
            testsSettings.testTimeInSeconds * MILLIS_IN_SECONDS+MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
				millisAfterStart=millisUntilFinished
                _leftFormattedTime.value = getFormattedLeftTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }

    private fun generateQuestion(questionNo: Int) {
        Log.e("generateQuestion", "cur: $questionNo, size: ${testInfo.countOfQuestions}")
        if (questionNo < testInfo.countOfQuestions) {
            val oldAnswerList=testQuestion[questionNo].answers
            val oldCorrectAnswers=testQuestion[questionNo].correct
            val oldCorrectAnswerString=oldAnswerList[oldCorrectAnswers[PARAMETER_ZERO]]
            val newAnswerList=randomElementsFromAnswersList(oldAnswerList,oldAnswerList.size)
            val newCorrectAnswerIndex=newAnswerList.indexOf(oldCorrectAnswerString)
            val newQuestion=testQuestion[questionNo].copy(answers=newAnswerList, correct = listOf(newCorrectAnswerIndex))
         // curQuestin=newQuestion
            _question.value=newQuestion
            //_question.value = newQuestion
        } else
            finishGame()
    }

    private fun finishGame() {
        _leftFormattedTime.value = getFormattedLeftTime(PARAMETER_ZERO_LONG)
        _gameResult.value = getGameResult()
    }

    private fun getGameResult(): TestsResult {
        val percentOfRightAnswers = getPercentOfRightAnswers()
        val enoughPercentage = percentOfRightAnswers >= testsSettings.minPercentOfRightAnswers
        val winner = enoughPercentage 
        val countOfQuestions = countOfRightAnswers + countOfWrongAnswers
        return TestsResult(
		testInfo.title,
			getFormattedLeftTime(testInfo.testTimeInSeconds*MILLIS_IN_SECONDS+MILLIS_IN_SECONDS-millisAfterStart),
			currentNoOfQuestion,
            winner,
            countOfRightAnswers,
            countOfQuestions,
            testsSettings,
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
        _percentOfRightAnswers.value =percentOfRightAnswers
        _percentOfRightAnswersStr.value = "$percentOfRightAnswers/${testInfo.minPercentOfRightAnswers}"
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
