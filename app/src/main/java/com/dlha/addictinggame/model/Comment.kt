package com.dlha.addictinggame.model

import com.google.gson.annotations.SerializedName

class Comment(@SerializedName("username") val user_comment : String,@SerializedName("time") val time_comment : String,@SerializedName ("content") val text_comment : String) {
}