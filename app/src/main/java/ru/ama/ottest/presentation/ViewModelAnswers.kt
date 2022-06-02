package ru.ama.ottest.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.ama.ottest.domain.entity.TestQuestion
import ru.ama.ottest.domain.usecase.GetAllQuestionsListUseCase
import javax.inject.Inject

class ViewModelAnswers @Inject constructor(
    private val getAllQuestionsListUseCase: GetAllQuestionsListUseCase
) : ViewModel() {

    fun getTestAnswers(testId: Int) {

        val d = viewModelScope.async(Dispatchers.IO) {
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
