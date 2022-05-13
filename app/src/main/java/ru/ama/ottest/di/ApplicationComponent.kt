package ru.ama.ottest.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.ama.ottest.presentation.*

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class//,
       // WorkerModule::class
    ]
)
interface ApplicationComponent {

    fun inject(activity: MainActivity)
    fun inject(fragment: GameFragment)
    fun inject(fragment: ChooseTestFragment)
  //  fun inject(activity: CoinPriceListActivity)

  //  fun inject(fragment: CoinDetailFragment)

    fun inject(application: MyApplication)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}
