package ru.ama.ottest.di

import dagger.Binds
import dagger.Module
import ru.ama.ottest.data.ExampleLocalDataSource
import ru.ama.ottest.data.ExampleLocalDataSourceImpl

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindLocalDataSource(impl: ExampleLocalDataSourceImpl): ExampleLocalDataSource

}
