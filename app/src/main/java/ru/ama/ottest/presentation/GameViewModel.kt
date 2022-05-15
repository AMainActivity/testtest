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
        //  loadDataUseCase()
        // getTInfo()
    }
    /* private val _readyStart = MutableLiveData<Unit>()
     val readyStart: LiveData<Unit>
         get() = _readyStart*/

    private fun getTInfo() {
        /* val d1 = viewModelScope.async(Dispatchers.IO) {
             getTestInfoUseCase(1)
         }*/

        /*val d2 = viewModelScope.async(Dispatchers.IO) {
            //testInfo = d1.await()[0]
            getQuestionsListUseCase(1, testInfo.countOfQuestions)

        }
        val dfd=viewModelScope.launch {

            //val d1 = viewModelScope.async(Dispatchers.IO) {
            //    getTestInfoUseCase(1)
            //}
val sdsd=d2.await()
            Log.e("getQuestionssdsd", sdsd.toString())
            testQuestion = sdsd

        }*/
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

    /* private val parentJob = SupervisorJob()
     private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
         Log.d("coroutineScope", "Exception caught: $throwable")
     }
     private val coroutineScope = CoroutineScope(Dispatchers.IO + parentJob + exceptionHandler)
 */

    val kolvoOfQuestions by lazy { testInfo.countOfQuestions }

    lateinit var testInfo: TestInfo //= getTestInfoUseCase(1)
    lateinit var testQuestion: List<TestQuestion>//= getQuestionsListUseCase(1)
    /*private val repository = repository1//GameRepositoryImpl()*/

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
    // lateinit var testInfo: LiveData<List<TestInfo>>

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    /* private var _currentNoOfQuestion = MutableLiveData<Int>()
     val currentNoOfQuestion: LiveData<Int>
         get() = _currentNoOfQuestion
 */
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
    private var listResultOfTest: MutableList<ResultOfTest> = mutableListOf<ResultOfTest>()

    fun startGame() {
        setupGameSettings()
        startTimer()
        // shuffleListOfQuestionsUserCase()
        _state.value = CurrentNoOfQuestion(currentNoOfQuestion)
        generateQuestion(currentNoOfQuestion/*_currentNoOfQuestion.value!!*/)
    }

    fun chooseAnswer(answer: Int) {
        if (gameResult.value != null) {
            return
        }
        checkAnswer(answer)
        getPercentOfRightAnswers()
        currentNoOfQuestion++
        _state.value = CurrentNoOfQuestion(currentNoOfQuestion)
//        _currentNoOfQuestion.value = _currentNoOfQuestion.value!! + 1
        generateQuestion(currentNoOfQuestion/*_currentNoOfQuestion.value!!*/)
        //_currentNoOfQuestion.value = _currentNoOfQuestion.value!! + 1
        //generateQuestion(_currentNoOfQuestion.value!!)
    }

    private fun setupGameSettings() {
        //gameSettings = getGameSettingsUseCase()
        //testInfo=getTestInfoUseCase()
        _state.value = MinPercentOfRightAnswers(gameSettings.minPercentOfRightAnswers)
    }

    private fun checkAnswer(answer: Int) {
        val rightAnswer = question.value
//        val rightAnswer2 = state.value as Question2
        if (answer == rightAnswer!!.correct[0]) {        /////answer.startsWith("*")  верный ответ начинается на *
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
        timer = object : CountDownTimer(
            gameSettings.testTimeInSeconds * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
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
            /*
            val tempQuestion=testQuestion[questionNo].copy(answers=randomElementsFromAnswersList(ans,ans.size))
             _question.value = tempQuestion
              val list=listOf("один","два","три","четыре","пять")
    val oldCorrect=listOf(2)
    val oldCorrectAnswer=list[oldCorrect[0]]
    val newList=randomElementsFromAnswersList(list,list.size)
    val newCorect=newList.indexOf(oldCorrectAnswer)
            */
            _question.value = newQuestion
        } else
            finishGame()
    }

    private fun finishGame() {
        _state.value = LeftFormattedTime(getFormattedLeftTime(0))
        _gameResult.value = getGameResult()
        //shuffleListOfQuestionsUserCase()
    }

    private fun getGameResult(): ru.ama.ottest.domain.entity.GameResult {
        val percentOfRightAnswers = getPercentOfRightAnswers()
        val enoughPercentage = percentOfRightAnswers >= gameSettings.minPercentOfRightAnswers
        //val enoughRightAnswers = countOfRightAnswers >= gameSettings.minCountOfRightAnswers
        val winner = enoughPercentage //&& enoughRightAnswers
        val countOfQuestions = countOfRightAnswers + countOfWrongAnswers
        return ru.ama.ottest.domain.entity.GameResult(
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
        _percentOfRightAnswers.value = percentOfRightAnswers
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
        // coroutineScope.cancel()
    }

    companion object {

        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTE = 60
    }
}
