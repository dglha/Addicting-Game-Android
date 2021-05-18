package com.dlha.addictinggame.api

import com.dlha.addictinggame.model.Category
import com.dlha.addictinggame.model.GameItem
import com.dlha.addictinggame.model.Games
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface AppServiceAPI {
    @POST("api/listdanhmuc")
    suspend fun getListCategories() : Response<Category>

    @GET("api/listNewGame")
    suspend fun getListNewGames(): Response<List<GameItem>>
}