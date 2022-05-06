package ru.ama.ottest.di

import androidx.lifecycle.ViewModel
import ru.ama.ottest.presentation.GameViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GameViewModel::class)
    fun bindCoinViewModel(viewModel: GameViewModel): ViewModel
}
