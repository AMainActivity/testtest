package ru.ama.ottest.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TestApiFactory {

    private const val BASE_URL = "https://kol.hhos.ru/test/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val apiService = retrofit.create(TestApiService::class.java)
}
