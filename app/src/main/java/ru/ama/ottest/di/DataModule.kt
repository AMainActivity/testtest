package ru.ama.ottest.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.ama.ottest.data.database.AppDatabase
import ru.ama.ottest.data.database.TestInfoDao
import ru.ama.ottest.data.database.TestQuestionsDao
import ru.ama.ottest.data.network.TestApiFactory
import ru.ama.ottest.data.network.TestApiService
import ru.ama.ottest.data.repository.TestsRepositoryImpl
import ru.ama.ottest.domain.repository.TestsRepository

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindCoinRepository(impl: TestsRepositoryImpl): TestsRepository

    companion object {

        @Provides
        @ApplicationScope
        fun provideTestDao(
            application: Application
        ): TestQuestionsDao {
            return AppDatabase.getInstance(application).testQuestionsDao()
        }

        @Provides
        @ApplicationScope
        fun provideInfoDao(
            application: Application
        ): TestInfoDao {
            return AppDatabase.getInstance(application).testInfoDao()
        }

        @Provides
        @ApplicationScope
        fun provideApiService(): TestApiService {
            return TestApiFactory.apiService
        }
    }
}
