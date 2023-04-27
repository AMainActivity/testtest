package ru.ama.ottest.presentation

import androidx.lifecycle.*
import kotlinx.coroutines.*
import ru.ama.ottest.domain.entity.*
import ru.ama.ottest.domain.usecase.*
import javax.inject.Inject

class TestListViewModel @Inject constructor(
    private val getTestInfoUseCase: GetTestInfoUseCase
) : ViewModel() {

    init {
        val d=viewModelScope.async(Dispatchers.IO)
        {
           getTestInfoUseCase()
        }

        viewModelScope.launch {
            val p=d.await()
            _testInfoDomModel.value = p
        }
    }
   private val _testInfoDomModel = MutableLiveData<List<TestInfoDomModel>>()
    val testInfoDomModel: LiveData<List<TestInfoDomModel>>
        get() = _testInfoDomModel

    companion object {}
}
