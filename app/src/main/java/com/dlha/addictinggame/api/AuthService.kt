package com.dlha.addictinggame.api

import com.dlha.addictinggame.model.Message
import com.dlha.addictinggame.model.User
import com.dlha.addictinggame.model.UserResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    @POST("api/login")
    @FormUrlEncoded
    suspend fun userLogin(@Field("username") username : String, @Field("password") password : String): Response<UserResponse>

    @POST("api/register")
    @FormUrlEncoded
    suspend fun userRegister(@Field("name") name : String, @Field("username") username : String, @Field("password") password: String): Response<Message>

    @POST("api/getUser")
    @FormUrlEncoded
    suspend fun getUserInfo(@Field("token") token: String): Response<User>

}