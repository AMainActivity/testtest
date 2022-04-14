package ru.ama.ottest.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.ama.ottest.presentation.GameFragment

@ApplicationScope
@Component(modules = [DomainModule::class,ViewModelModule::class,DataModule::class])
interface ApplicationComponent {

    fun inject(fragment: GameFragment)
	
	 @Component.Factory
    interface ApplicationComponentFactory {

        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }
}
