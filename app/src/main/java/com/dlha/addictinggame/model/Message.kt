package com.dlha.addictinggame.model

import com.google.gson.annotations.SerializedName

class Message(@SerializedName("code") val code : Int,@SerializedName("message") val message: String) {

}