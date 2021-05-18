package com.dlha.addictinggame.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
    val time: String,
    @SerializedName("sale")
    val saleCoin: String,
    @SerializedName("developer")
    val developer: String,
): Parcelable