package ru.ama.ottest.data


import javax.inject.Inject

class ExampleLocalDataSourceImpl @Inject constructor(
    private val database: ExampleDatabase
) : ExampleLocalDataSource {

    override fun method():String {
       return database.method()
    }
}
