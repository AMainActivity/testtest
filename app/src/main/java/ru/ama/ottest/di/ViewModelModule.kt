package ru.ama.ottest.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.ama.ottest.presentation.ViewModelAnswers
import ru.ama.ottest.presentation.ViewModelSplash
import ru.ama.ottest.presentation.ViewModelTestList
import ru.ama.ottest.presentation.ViewModelTestProcess

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelSplash::class)
    fun bindSplashViewModel(viewModel: ViewModelSplash): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelTestProcess::class)
    fun bindTestViewModel(viewModel: ViewModelTestProcess): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelTestList::class)
    fun bindTestsViewModel(viewModel: ViewModelTestList): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelAnswers::class)
    fun bindViewModelAnswers(viewModel: ViewModelAnswers): ViewModel
}
