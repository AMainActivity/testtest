package ru.ama.ottest.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TestJsonDto(
    @Expose
    @SerializedName("error"   ) val error   : Boolean,
    @Expose
    @SerializedName("message" ) val message : String,
    @Expose
    @SerializedName("questions"    ) val questions    : List<TestQuestionsDto>

)
