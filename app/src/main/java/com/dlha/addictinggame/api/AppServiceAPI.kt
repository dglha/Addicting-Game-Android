package com.dlha.addictinggame.api

import com.dlha.addictinggame.model.Category
import com.dlha.addictinggame.model.GameItem
import com.dlha.addictinggame.model.Games
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface AppServiceAPI {
    @GET("api/listCategory")
    suspend fun getListCategories(): Response<List<Category>>

    @GET("api/listNewGame")
    suspend fun getListNewGames(): Response<List<GameItem>>

    @GET("api/listGameSale")
    suspend fun getListSaleGame(): Response<List<GameItem>>
}