package ru.ama.ottest.presentation

import android.app.Application
import androidx.work.Configuration
import ru.ama.ottest.di.DaggerApplicationComponent
import javax.inject.Inject

class MyApplication : Application() {
 //@Inject
   // lateinit var workerFactory: TestWorkerFactory
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
	 
	 override fun onCreate() {
        component.inject(this)
        super.onCreate()
    }

    /*override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }*/
}
