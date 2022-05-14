package ru.ama.ottest.presentation

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*
import ru.ama.ottest.domain.entity.*
import ru.ama.ottest.domain.usecase.*
import javax.inject.Inject

class TestsViewModel @Inject constructor(
    private val getTestInfoUseCase: GetTestInfoUseCase
) : ViewModel() {

    init {
        val d=viewModelScope.async(Dispatchers.IO)
        {
            val r=getTestInfoUseCase()
            r
        }

        viewModelScope.launch {
            val p=d.await()
            Log.e("ppp",p.toString())
            _testInfo.value = p
        }
    }
   private val _testInfo = MutableLiveData<List<TestInfo>>()
    val testInfo: LiveData<List<TestInfo>>
        get() = _testInfo

    companion object {}
}
