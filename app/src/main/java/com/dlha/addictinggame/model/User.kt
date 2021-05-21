package com.dlha.addictinggame.model


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("allcoin")
    val allcoin: Int,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("coinhave")
    val coinhave: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("firstname")
    val firstname: String,
    @SerializedName("lastname")
    val lastname: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("total_favorite")
    val totalFavorites: Int,
)