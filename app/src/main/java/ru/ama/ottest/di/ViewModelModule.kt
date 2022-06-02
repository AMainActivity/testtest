package ru.ama.ottest.di

import androidx.lifecycle.ViewModel
import ru.ama.ottest.presentation.TestProcessViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.ama.ottest.presentation.SplashViewModel
import ru.ama.ottest.presentation.TestListViewModel
import ru.ama.ottest.presentation.ViewModelAnswers

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TestProcessViewModel::class)
    fun bindTestViewModel(viewModel: TestProcessViewModel): ViewModel
	
    @Binds
    @IntoMap
    @ViewModelKey(TestListViewModel::class)
    fun bindTestsViewModel(viewModel: TestListViewModel): ViewModel
	
    @Binds
    @IntoMap
    @ViewModelKey(ViewModelAnswers::class)
    fun bindViewModelAnswers(viewModel: ViewModelAnswers): ViewModel
}
