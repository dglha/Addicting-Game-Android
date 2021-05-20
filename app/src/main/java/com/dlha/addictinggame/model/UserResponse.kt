package com.dlha.addictinggame.model

import com.google.gson.annotations.SerializedName

class UserResponse(
    @SerializedName("token") val token: String,
    @SerializedName("username") val username: String,
    @SerializedName("avatar") val avatar: String
) {

}