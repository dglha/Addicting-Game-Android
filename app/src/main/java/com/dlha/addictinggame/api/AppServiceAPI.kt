package com.dlha.addictinggame.api

import com.dlha.addictinggame.model.Category
import com.dlha.addictinggame.model.Comment
import com.dlha.addictinggame.model.GameItem

import retrofit2.Response
import retrofit2.http.*

interface AppServiceAPI {
    @GET("api/listCategory")
    suspend fun getListCategories(): Response<List<Category>>

    @GET("api/listNewGame")
    suspend fun getListNewGames(@Query("token") token : String): Response<List<GameItem>>

    @GET("api/listGameSale")
    suspend fun getListSaleGame(): Response<List<GameItem>>

    @GET("api/listGameInCategory")
    suspend fun getListGameInCategory(@Query("iddm") idcategory : Int ): Response<List<GameItem>>

    @GET("api/getListCommentInGame")
    suspend fun getListCommentInGame(@Query("idgame") idgame : Int): Response<List<Comment>>
}