package com.dlha.addictinggame.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object
    {
        fun getRetrofit() : Retrofit {
            val httpLoggingInterceptor : HttpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient : OkHttpClient = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()

            val retrofit : Retrofit = Retrofit
                                        .Builder()
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .baseUrl("https://whynotaddicting.000webhostapp.com/")
                                        .client(okHttpClient).build()
            return retrofit
        }

    }
}