package com.dlha.addictinggame.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt

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
    val link: String?,
    @SerializedName("namegame")
    val name: String,
    @SerializedName("time")
    val time: String?,
    @SerializedName("sale")
    val salePercent: String,
    @SerializedName("developer")
    val developer: String,
    var newCoin : Int,
    @SerializedName("isBuy") val isBuy : Int,
    @SerializedName("isFavorite") val isFavorite : Int
): Parcelable {
}