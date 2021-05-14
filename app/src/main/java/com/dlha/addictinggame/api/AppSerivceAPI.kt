package com.dlha.addictinggame.api

import com.dlha.addictinggame.model.Category
import retrofit2.Call
import retrofit2.http.POST

interface AppSerivceAPI {
    @POST("api/listdanhmuc")
    fun getListCategories() : Call<Category>
}