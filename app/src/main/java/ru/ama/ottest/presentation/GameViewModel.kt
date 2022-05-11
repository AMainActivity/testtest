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

    init {
      //  loadDataUseCase()
        getTInfo()
    }
   /* private val _readyStart = MutableLiveData<Unit>()
    val readyStart: LiveData<Unit>
        get() = _readyStart*/

    private fun getTInfo() {
        val d1 = viewModelScope.async(Dispatchers.IO) {
            getTestInfoUseCase(1)
        }

        viewModelScope.launch {

            /*val d1 = viewModelScope.async(Dispatchers.IO) {
                getTestInfoUseCase(1)
            }*/
            testInfo = d1.await()[0]
            val d2 = viewModelScope.async(Dispatchers.IO) {
            getQuestionsListUseCase(1,testInfo.countOfQuestions)
        }
            testQuestion = d2.await()
             _state.value=ReadyStart
            Log.e("getTestInfoUseCase", testInfo.toString())
            Log.e("getQuestionsListUseCase", testQuestion[0].toString())
        }
    }

	val kolvoOfQuestions by lazy{ testInfo.countOfQuestions}
	
    lateinit var testInfo:TestInfo //= getTestInfoUseCase(1)
    lateinit var testQuestion :List<TestQuestion>//= getQuestionsListUseCase(1)
    /*private val repository = repository1//GameRepositoryImpl()*/


    private val gameSettings: GameSettings by lazy{ GameSettings(
        testInfo.minCountOfRightAnswers,
        testInfo.minPercentOfRightAnswers,
        testInfo.testTimeInSeconds
    )}
    // lateinit var testInfo: LiveData<List<TestInfo>>
    //private var currentNoOfQuestion: Int=-1

private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state
  private var _currentNoOfQuestion = MutableLiveData<Int>()
    val currentNoOfQuestion: LiveData<Int>
        get() = _currentNoOfQuestion
 /*
    private val _minPercentOfRightAnswers = MutableLiveData<Int>()
    val minPercentOfRightAnswers: LiveData<Int>
        get() = _minPercentOfRightAnswers

    private val _leftFormattedTime = MutableLiveData<String>()
    val leftFormattedTime: LiveData<String>
        get() = _leftFormattedTime
*/
    private val _question = MutableLiveData<TestQuestion>()
    val question: LiveData<TestQuestion>
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
        // shuffleListOfQuestionsUserCase()
        _currentNoOfQuestion.value=0
        generateQuestion(_currentNoOfQuestion.value!!)
    }

    fun chooseAnswer(answer: Int) {
        if (gameResult.value != null) {
            return
        }
        checkAnswer(answer)
        getPercentOfRightAnswers()
        _currentNoOfQuestion.value = _currentNoOfQuestion.value!! + 1
        generateQuestion(_currentNoOfQuestion.value!!)
        //_currentNoOfQuestion.value = _currentNoOfQuestion.value!! + 1
        //generateQuestion(_currentNoOfQuestion.value!!)
    }

    private fun setupGameSettings() {
        //gameSettings = getGameSettingsUseCase()
        //testInfo=getTestInfoUseCase()
        _state.value=MinPercentOfRightAnswers(gameSettings.minPercentOfRightAnswers)
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
               _state.value=LeftFormattedTime( getFormattedLeftTime(millisUntilFinished))
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }

    private fun generateQuestion(questionNo: Int) {
        Log.e("generateQuestion", "cur: $questionNo, size: ${testInfo.countOfQuestions}")
        if (questionNo < testInfo.countOfQuestions)
            _question.value= testQuestion[questionNo]
        else
            finishGame()
    }

    private fun finishGame() {
        _state.value=LeftFormattedTime(getFormattedLeftTime(0))
        _gameResult.value= getGameResult()
        //shuffleListOfQuestionsUserCase()
    }

    private fun getGameResult(): ru.ama.ottest.domain.entity.GameResult {
        val percentOfRightAnswers = getPercentOfRightAnswers()
        val enoughPercentage = percentOfRightAnswers >= gameSettings.minPercentOfRightAnswers
        val enoughRightAnswers = countOfRightAnswers >= gameSettings.minCountOfRightAnswers
        val winner = enoughPercentage && enoughRightAnswers
        val countOfQuestions = countOfRightAnswers + countOfWrongAnswers
        return ru.ama.ottest.domain.entity.GameResult(winner, countOfRightAnswers, countOfQuestions, gameSettings)
    }

    private fun getPercentOfRightAnswers(): Int {
        val countOfQuestions = countOfRightAnswers + countOfWrongAnswers
        val percentOfRightAnswers = if (countOfQuestions > 0) {
            ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
        } else {
            0
        }
        _percentOfRightAnswers.value=percentOfRightAnswers
        return percentOfRightAnswers
    }

    private fun getFormattedLeftTime(millisUntilFinished: Long): String {
		/*
		 val minutes = milliseconds / 1000 / 60
    val seconds = milliseconds / 1000 % 60
		*/		
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
