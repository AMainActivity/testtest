package ru.ama.ottest.di

import ru.ama.ottest.data.workers.ChildWorkerFactory
import ru.ama.ottest.data.workers.TestRefreshDataWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(TestRefreshDataWorker::class)
    fun bindRefreshDataWorkerFactory(worker: TestRefreshDataWorker.Factory): ChildWorkerFactory
}
