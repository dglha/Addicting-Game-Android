package com.dlha.addictinggame.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    @SerializedName("iddanhmuc") val id: Int,
    @SerializedName("tendanhmuc") val nameCategory: String,
    @SerializedName("imgdanhmuc") val categoryImage: String
) : Parcelable {

}