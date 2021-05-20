package com.dlha.addictinggame.api

import com.dlha.addictinggame.model.UserResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserServiceAPI {
    @POST("api/getUser")
    @FormUrlEncoded
    suspend fun getUserInformation(@Field("token") token : String): Response<UserResponse>

}