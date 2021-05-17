package com.dlha.addictinggame.di

import com.dlha.addictinggame.api.AppServiceAPI
import com.dlha.addictinggame.api.AuthService
import com.dlha.addictinggame.api.UserServiceAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }


    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://whynotaddicting.000webhostapp.com/")
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideAppServiceAPI(retrofit: Retrofit): AppServiceAPI{
        return retrofit.create(AppServiceAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideAutServiceAPI(retrofit: Retrofit): AuthService{
        return retrofit.create(AuthService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserServiceAPI(retrofit: Retrofit): UserServiceAPI{
        return retrofit.create(UserServiceAPI::class.java)
    }


}