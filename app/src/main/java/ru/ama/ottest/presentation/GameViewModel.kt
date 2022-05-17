package ru.ama.ottest.presentation

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*
import ru.ama.ottest.domain.entity.*
import ru.ama.ottest.domain.usecase.*
import javax.inject.Inject

class GameViewModel @Inject constructor(
    private val getQuestionsListUseCase: GetQuestionsListUseCase,
    private val loadDataUseCase: LoadDataUseCase,
    private val getTestInfoUseCase: GetTestInfoUseCase
) : ViewModel() {

    private var currentNoOfQuestion: Int = -1

    init {
        currentNoOfQuestion = 0
    }
    /* private val _readyStart = MutableLiveData<Unit>()
     val readyStart: LiveData<Unit>
         get() = _readyStart*/

    private fun getTInfo() {
     
        val d=viewModelScope.async(Dispatchers.IO) {
            // dfd.join()
            getQuestionsListUseCase(testInfo.testId, testInfo.countOfQuestions)

        }
        viewModelScope.launch {
            testQuestion = d.await()
                Log.e("getTestInfoUseCase", testInfo.toString())
            Log.e("getQuestionsListUseCase", testQuestion[0].toString())
            _state.postValue(ReadyStart)
        }
    }


    val kolvoOfQuestions by lazy { testInfo.countOfQuestions }

    lateinit var testInfo: TestInfo 
    lateinit var testQuestion: List<TestQuestion>

    fun setParams(tInfo: TestInfo) {
        testInfo = tInfo
        getTInfo()
    }

    private val gameSettings: GameSettings by lazy {
        GameSettings(
            testInfo.minPercentOfRightAnswers,
            testInfo.testTimeInSeconds
        )
    }

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state
   
  
    private val _question = MutableLiveData<TestQuestion>()
    val question: LiveData<TestQuestion>
        get() = _question

   /* private val _rightAnswerLD = MutableLiveData<Boolean>()
    val rightAnswerLD: LiveData<Boolean>
        get() = _rightAnswerLD*/

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult
		
    private var millisAfterStart = 0L

    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers
    private val _percentOfRightAnswersStr = MutableLiveData<String>()
    val percentOfRightAnswersStr: LiveData<String>
        get() = _percentOfRightAnswersStr

    val enoughPercentage: LiveData<Boolean> = Transformations.map(percentOfRightAnswers) {
        it >= gameSettings.minPercentOfRightAnswers
    }

    private var timer: CountDownTimer? = null
    private var countOfRightAnswers = 0
    private var countOfWrongAnswers = 0
    private var listResultOfTest: MutableList<ResultOfTest> = mutableListOf<ResultOfTest>()

    fun startGame() {
        startTimer()
        _state.value = CurrentNoOfQuestion(currentNoOfQuestion)
        generateQuestion(currentNoOfQuestion)
    }

    fun chooseAnswer(answer: Int) {
        if (gameResult.value != null) {
            return
        }
        checkAnswer(answer)
        getPercentOfRightAnswers()
        currentNoOfQuestion++
        _state.value = CurrentNoOfQuestion(currentNoOfQuestion)
        generateQuestion(currentNoOfQuestion)
    }


    private fun checkAnswer(answer: Int) {
        val rightAnswer = question.value
      //  _rightAnswerLD.value=answer == rightAnswer!!.correct[0]
        if (answer == rightAnswer!!.correct[0]) {        
            countOfRightAnswers++
        } else {
            countOfWrongAnswers++
            val rOt = ResultOfTest(
                currentNoOfQuestion+1,
                rightAnswer.question,
                rightAnswer.imageUrl,
                rightAnswer.answers,
                answer,
                rightAnswer.correct[0]
            )
            listResultOfTest.add(rOt)
        }
    }


    private fun randomElementsFromAnswersList(list: List<String>, randCount: Int): List<String> {
        return list.asSequence().shuffled().take(randCount).toList()
    }

    private fun startTimer() {
		millisAfterStart=0L
        timer = object : CountDownTimer(
            gameSettings.testTimeInSeconds * MILLIS_IN_SECONDS+MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
				millisAfterStart=millisUntilFinished
                _state.value = LeftFormattedTime(getFormattedLeftTime(millisUntilFinished))
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
            val oldCorrectAnswerString=oldAnswerList[oldCorrectAnswers[0]]
            val newAnswerList=randomElementsFromAnswersList(oldAnswerList,oldAnswerList.size)
            val newCorrectAnswerIndex=newAnswerList.indexOf(oldCorrectAnswerString)
            val newQuestion=testQuestion[questionNo].copy(answers=newAnswerList, correct = listOf(newCorrectAnswerIndex))
          
            _question.value = newQuestion
        } else
            finishGame()
    }

    private fun finishGame() {
        _state.value = LeftFormattedTime(getFormattedLeftTime(0))
        _gameResult.value = getGameResult()
    }

    private fun getGameResult(): GameResult {
        val percentOfRightAnswers = getPercentOfRightAnswers()
        val enoughPercentage = percentOfRightAnswers >= gameSettings.minPercentOfRightAnswers
        val winner = enoughPercentage 
        val countOfQuestions = countOfRightAnswers + countOfWrongAnswers
        return GameResult(
			getFormattedLeftTime(testInfo.testTimeInSeconds*MILLIS_IN_SECONDS+MILLIS_IN_SECONDS-millisAfterStart),
			currentNoOfQuestion,
            winner,
            countOfRightAnswers,
            countOfQuestions,
            gameSettings,
            listResultOfTest
        )
    }

    private fun getPercentOfRightAnswers(): Int {
        val countOfQuestions = countOfRightAnswers + countOfWrongAnswers
        val percentOfRightAnswers = if (countOfQuestions > 0) {
            ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
        } else {
            0
        }
        _percentOfRightAnswers.value =percentOfRightAnswers
        _percentOfRightAnswersStr.value = "$percentOfRightAnswers/${testInfo.minPercentOfRightAnswers}"
        return percentOfRightAnswers
    }

    private fun getFormattedLeftTime(millisUntilFinished: Long): String {
        
        val seconds = (millisUntilFinished / MILLIS_IN_SECONDS % SECONDS_IN_MINUTE).toInt()
        val minutes = millisUntilFinished / MILLIS_IN_SECONDS / SECONDS_IN_MINUTE
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
