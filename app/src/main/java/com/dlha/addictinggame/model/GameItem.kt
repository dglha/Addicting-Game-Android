package com.dlha.addictinggame.model


import com.google.gson.annotations.SerializedName

data class GameItem(
    @SerializedName("coin")
    val coin: String,
    @SerializedName("detailgame")
    val detail: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("iddm")
    val categoryId: Int,
    @SerializedName("imggame")
    val image: String,
    @SerializedName("imgtoshow")
    val imgtoshow: String?,
    @SerializedName("linkgame")
    val link: String,
    @SerializedName("namegame")
    val name: String,
    @SerializedName("time")
    val time: String
)