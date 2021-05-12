package com.dlha.addictinggame.api

import com.dlha.addictinggame.model.Message
import com.dlha.addictinggame.model.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    @POST("api/login")
    @FormUrlEncoded
    fun userLogin(@Field("username") username : String, @Field("password") password : String): Call<User>

    @POST("api/register")
    @FormUrlEncoded
    fun userRegister(@Field("name") name : String, @Field("username") username : String, @Field("password") password: String): Call<Message>

}