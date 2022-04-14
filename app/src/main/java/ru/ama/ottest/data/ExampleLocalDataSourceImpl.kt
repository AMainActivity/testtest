package ru.ama.ottest.data


import android.util.Log
import javax.inject.Inject

class ExampleLocalDataSourceImpl @Inject constructor(
    private val database: ExampleDatabase
) : ExampleLocalDataSource {

    override fun method():String {
      //  Log.d("ExampleLocalDataSourceImpl", database.method())
       return database.method()
    }
}
