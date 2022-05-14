package ru.ama.ottest.di

import androidx.lifecycle.ViewModel
import ru.ama.ottest.presentation.GameViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.ama.ottest.presentation.SplashViewModel
import ru.ama.ottest.presentation.TestsViewModel

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GameViewModel::class)
    fun bindCoinViewModel(viewModel: GameViewModel): ViewModel
	
    @Binds
    @IntoMap
    @ViewModelKey(TestsViewModel::class)
    fun bindTestsViewModel(viewModel: TestsViewModel): ViewModel
}
