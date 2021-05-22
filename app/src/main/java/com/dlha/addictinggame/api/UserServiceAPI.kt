package com.dlha.addictinggame.api

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

    @POST("api/addComment")
    @FormUrlEncoded
    suspend fun addComment(@Field("token") token : String,@Field("content") text_comment : String,@Field("idgame") idgame : Int): Response<Message>
}