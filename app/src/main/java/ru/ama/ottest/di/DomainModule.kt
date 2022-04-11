package ru.ama.ottest.di

import dagger.Binds
import dagger.Module
import ru.ama.ottest.data.GameRepositoryImpl
import ru.ama.ottest.domain.repository.GameRepository

@Module
interface DomainModule {

    @Binds
    fun bindRepository(impl: GameRepositoryImpl): GameRepository
}
