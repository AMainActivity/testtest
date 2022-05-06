package ru.ama.ottest.data.network

import ru.ama.ottest.data.network.model.TestJsonDto
import retrofit2.http.GET
import retrofit2.http.Query

interface TestApiService {

    @GET("gettest.php")
    suspend fun getTestList(
        @Query(QUERY_PARAM_TEST_ID) tid: String = "1"
    ): TestJsonDto

    companion object {
        private const val QUERY_PARAM_TEST_ID = "tid"
    }
}