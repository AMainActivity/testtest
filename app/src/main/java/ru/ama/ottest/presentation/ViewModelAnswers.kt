package ru.ama.ottest.presentation

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*
import ru.ama.ottest.domain.entity.*
import ru.ama.ottest.domain.usecase.*
import javax.inject.Inject

class ViewModelAnswers @Inject constructor(
    private val getAllQuestionsListUseCase: GetAllQuestionsListUseCase
) : ViewModel() {

      fun getTestAnswers(testId:Int) {
     
        val d=viewModelScope.async(Dispatchers.IO) {
            getAllQuestionsListUseCase(testId)

        }
        viewModelScope.launch {
            _listOfAnswers.postValue(d.await())
        }
    }
	
	
	
   private val _listOfAnswers = MutableLiveData<List<TestQuestion>>()
    val listOfAnswers: LiveData<List<TestQuestion>>
        get() = _listOfAnswers

    companion object {}
}
