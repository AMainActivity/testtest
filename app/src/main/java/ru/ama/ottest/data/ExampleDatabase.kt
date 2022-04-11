package ru.ama.ottest.data


import android.content.Context
import android.util.Log
import javax.inject.Inject

class ExampleDatabase @Inject constructor(
    private val context: Context
) {

    fun method(): String {
        val ss=context.assets.open("ot.json").bufferedReader().readText()
        Log.d(LOG_TAG, "ExampleDatabase $ss")
    return ss
    }

    companion object {

        private const val LOG_TAG = "EXAMPLE_TEST"
    }
}
