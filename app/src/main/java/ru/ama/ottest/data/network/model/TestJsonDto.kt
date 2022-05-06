package ru.ama.ottest.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TestJsonDto(
    @Expose
    @SerializedName("error"   ) var error   : Boolean,
    @Expose
    @SerializedName("message" ) var message : String,
    @Expose
    @SerializedName("test"    ) var testData    : TestDataDto

)
