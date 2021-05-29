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
    suspend fun getListSaleGame(@Query("token") token : String): Response<List<GameItem>>

    @GET("api/listGameInCategory")
    suspend fun getListGameInCategory(@Query("iddm") idcategory : Int,@Query("token") token : String): Response<List<GameItem>>

    @GET("api/getListCommentInGame")
    suspend fun getListCommentInGame(@Query("idgame") idgame : Int): Response<List<Comment>>

    @GET("api/searchGame")
    suspend fun getListSearchGame(@Query("token") token : String,@Query("namegame") namegame : String) : Response<List<GameItem>>
}