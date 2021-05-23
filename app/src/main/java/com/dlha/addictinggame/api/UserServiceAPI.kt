package com.dlha.addictinggame.api

import com.dlha.addictinggame.model.GameItem
import com.dlha.addictinggame.model.Message
import com.dlha.addictinggame.model.UserResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserServiceAPI {
    @POST("api/getUser")
    @FormUrlEncoded
    suspend fun getUserInformation(@Field("token") token : String): Response<UserResponse>

    @POST("api/listGameFavorite")
    @FormUrlEncoded
    suspend fun getUserFavoriteListGames(@Field("token") token: String): Response<List<GameItem>>

    @POST("api/unFavoriteGame")
    @FormUrlEncoded
    suspend fun unFavoriteGameHaveId(@Field("token") token: String, @Field("idgame") id: Int): Response<Message>

    @POST("api/favoriteGame")
    @FormUrlEncoded
    suspend fun addFavoriteGameHaveId(@Field("token") token: String, @Field("idgame") id: Int): Response<Message>

    @POST("api/addComment")
    @FormUrlEncoded
    suspend fun addComment(@Field("token") token : String,@Field("content") text_comment : String,@Field("idgame") idgame : Int): Response<Message>

    @POST("api/listGameInCart")
    @FormUrlEncoded
    suspend fun listGameInCart(@Field("token") token: String) : Response<List<GameItem>>

    @POST("api/buyGame")
    @FormUrlEncoded
    suspend fun buyGame(@Field("token") token : String,@Field("idgame") idgame : Int) : Response<Message>

    @POST("api/removeGameCart")
    @FormUrlEncoded
    suspend fun removeGameCart(@Field("token") token : String,@Field("idgame") idgame : Int) : Response<Message>
}