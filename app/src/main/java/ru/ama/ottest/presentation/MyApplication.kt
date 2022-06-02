package ru.ama.ottest.presentation

import android.app.Application
import ru.ama.ottest.di.DaggerApplicationComponent

class MyApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}
