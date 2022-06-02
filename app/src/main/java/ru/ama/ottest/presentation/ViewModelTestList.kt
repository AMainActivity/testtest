package ru.ama.ottest.presentation

import android.os.CountDownTimer
import androidx.lifecycle.*
import kotlinx.coroutines.*
import ru.ama.ottest.domain.entity.*
import ru.ama.ottest.domain.usecase.*
import javax.inject.Inject

class ViewModelTestList @Inject constructor(
    private val getTestInfoUseCase: GetTestInfoUseCase
) : ViewModel() {

    init {
        val d=viewModelScope.async(Dispatchers.IO)
        {
           getTestInfoUseCase()
        }

        viewModelScope.launch {
            val p=d.await()
            _testInfo.value = p
        }
    }
   private val _testInfo = MutableLiveData<List<TestInfo>>()
    val testInfo: LiveData<List<TestInfo>>
        get() = _testInfo

    companion object {}
}
