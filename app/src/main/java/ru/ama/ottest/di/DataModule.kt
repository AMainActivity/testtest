package ru.ama.ottest.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.ama.ottest.data.database.AppDatabase
import ru.ama.ottest.data.database.TestInfoDao
import ru.ama.ottest.data.database.QuestionDao
import ru.ama.ottest.data.network.TestApiFactory
import ru.ama.ottest.data.network.TestApiService
import ru.ama.ottest.data.repository.TestRepositoryImpl
import ru.ama.ottest.domain.repository.TestRepository

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindCoinRepository(impl: TestRepositoryImpl): TestRepository

    companion object {

        @Provides
        @ApplicationScope
        fun provideTestDao(
            application: Application
        ): QuestionDao {
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
