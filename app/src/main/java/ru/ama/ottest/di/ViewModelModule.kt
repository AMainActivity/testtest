package ru.ama.ottest.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.ama.ottest.presentation.AnswersViewModel
import ru.ama.ottest.presentation.SplashViewModel
import ru.ama.ottest.presentation.TestListViewModel
import ru.ama.ottest.presentation.TestingViewModel

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TestingViewModel::class)
    fun bindTestViewModel(viewModel: TestingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TestListViewModel::class)
    fun bindTestsViewModel(viewModel: TestListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AnswersViewModel::class)
    fun bindViewModelAnswers(viewModel: AnswersViewModel): ViewModel
}
