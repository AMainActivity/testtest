package ru.ama.ottest.data

import android.content.Context
import javax.inject.Inject

class AssetProvider @Inject constructor(
    private val context: Context
) {

    fun getDescription() = context.assets.open("ot.json").bufferedReader().readText()//
// context.assets.open("tab1/item1/description.txt")
}