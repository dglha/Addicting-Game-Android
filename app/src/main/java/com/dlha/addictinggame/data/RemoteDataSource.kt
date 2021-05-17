package com.dlha.addictinggame.data

import com.dlha.addictinggame.api.AppServiceAPI
import com.dlha.addictinggame.api.AuthService
import com.dlha.addictinggame.api.UserServiceAPI
import com.dlha.addictinggame.model.Category
import com.dlha.addictinggame.model.Message
import com.dlha.addictinggame.model.User
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val appServiceAPI: AppServiceAPI,
    private val authService: AuthService,
    private val userServiceAPI: UserServiceAPI
) {

    //AppServiceAPI Code Here
    suspend fun getListCategories() : Response<Category>{
        return appServiceAPI.getListCategories()
    }

    /*
    *
    * AutServiceAPI Code Here
    *
    * */

    suspend fun userLogin(username : String, password: String): Response<User>{
        return authService.userLogin(username, password)
    }


    suspend fun userRegister(name: String, username: String, password: String): Response<Message>{
        return authService.userRegister(name, username, password)
    }

    /*
    *
    * UserServiceAPI Code Here
    *
    * */


    suspend fun getUserInformation(token: String): Response<User>{
        return userServiceAPI.getUserInformation(token)
    }

}