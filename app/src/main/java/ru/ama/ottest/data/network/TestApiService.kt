package ru.ama.ottest.data.network

import ru.ama.ottest.data.network.model.TestJsonDto
import retrofit2.http.GET
import retrofit2.http.Query
import ru.ama.ottest.data.network.model.TestListJsonDto

interface TestApiService {

    @GET("gettestbyid.php")
    suspend fun getTestById(
        @Query(QUERY_PARAM_TEST_ID) tid: String = "1"
    ): TestJsonDto

    @GET("gettestlist.php")
    suspend fun getTestList(): TestListJsonDto
	
    companion object {
        private const val QUERY_PARAM_TEST_ID = "tid"
    }
}