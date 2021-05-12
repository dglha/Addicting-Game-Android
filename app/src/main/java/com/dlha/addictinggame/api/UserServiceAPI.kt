package com.dlha.addictinggame.api

import com.dlha.addictinggame.model.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserServiceAPI {
    @POST("api/getUser")
    @FormUrlEncoded
    fun getUserDescription(@Field("token") token : String): Call<User>
}