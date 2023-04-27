package ru.ama.ottest.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.ama.ottest.presentation.*

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(fragment: TestingFragment)
    fun inject(fragment: TestListFragment)
    fun inject(fragment: AnswersFragment)
    fun inject(activity: SplashActivity)

    fun inject(application: MyApplication)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}
