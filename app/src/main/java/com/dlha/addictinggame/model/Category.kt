package com.dlha.addictinggame.model

import com.google.gson.annotations.SerializedName

class Category(@SerializedName("id") val id : Int,@SerializedName("tendanhmuc") val nameCategory: String,@SerializedName("hinhdanhmuc") val categoryImage : String) {
}