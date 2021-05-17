package com.dlha.addictinggame.api

import com.dlha.addictinggame.model.Category
import retrofit2.Call
import retrofit2.http.POST

interface AppServiceAPI {
    @POST("api/listdanhmuc")
    fun getListCategories() : Call<Category>
}