package ru.ama.ottest.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.ama.ottest.domain.usecase.LoadTestsFromNetUseCase
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val loadTestsFromNetUseCase: LoadTestsFromNetUseCase
) : ViewModel() {

    init {
        val d1 = viewModelScope.async(Dispatchers.IO) {
            loadTestsFromNetUseCase()
        }

        viewModelScope.launch {
            val f = d1.await()
            _canStart.value = Unit
        }
    }

    private val _canStart = MutableLiveData<Unit>()
    val canStart: LiveData<Unit>
        get() = _canStart

    companion object {}
}
