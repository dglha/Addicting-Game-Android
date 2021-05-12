package com.dlha.addictinggame.model

import com.google.gson.annotations.SerializedName

class User(@SerializedName("token") val token : String, @SerializedName("username") val username : String, @SerializedName("avatar") val avatar : String) {

}