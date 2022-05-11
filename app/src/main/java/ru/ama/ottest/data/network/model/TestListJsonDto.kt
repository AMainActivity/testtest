package ru.ama.ottest.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TestListJsonDto(
    @Expose
    @SerializedName("error"   ) var error   : Boolean,
    @Expose
    @SerializedName("message" ) var message : String,
    @Expose
    @SerializedName("testList"    ) var testListData    : List<TestListDataDto>

)
