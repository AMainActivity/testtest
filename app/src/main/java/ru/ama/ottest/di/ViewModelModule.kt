package ru.ama.ottest.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.ama.ottest.presentation.GameViewModel

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GameViewModel::class)
    fun bindMainViewModel(viewModel: GameViewModel): ViewModel
}
