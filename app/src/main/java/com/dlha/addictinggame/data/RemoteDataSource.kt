package com.dlha.addictinggame.data

import com.dlha.addictinggame.api.AppServiceAPI
import com.dlha.addictinggame.api.AuthService
import com.dlha.addictinggame.api.UserServiceAPI
import com.dlha.addictinggame.model.*
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val appServiceAPI: AppServiceAPI,
    private val authService: AuthService,
    private val userServiceAPI: UserServiceAPI
) {

    //AppServiceAPI Code Here
    suspend fun getListCategories() : Response<List<Category>>{
        return appServiceAPI.getListCategories()
    }

    suspend fun getListNewGames() : Response<List<GameItem>>{
        return appServiceAPI.getListNewGames()
    }

    suspend fun getListSaleGames(): Response<List<GameItem>> {
        return appServiceAPI.getListSaleGame()
    }
    suspend fun getListGameInCategory(iddm : Int): Response<List<GameItem>> {
        return  appServiceAPI.getListGameInCategory(iddm)
    }
    suspend fun getListCommentInGame(idgame : Int): Response<List<Comment>> {
        return appServiceAPI.getListCommentInGame(idgame)
    }

    /*
    *
    * AutServiceAPI Code Here
    *
    * */

    suspend fun userLogin(username : String, password: String): Response<UserResponse>{
        return authService.userLogin(username, password)
    }


    suspend fun userRegister(email: String, username: String, password: String): Response<Message>{
        return authService.userRegister(email, username, password)
    }

    suspend fun getUserInfo(token: String): Response<User>{
        return authService.getUserInfo(token)
    }

    /*
    *
    * UserServiceAPI Code Here
    *
    * */


    suspend fun getUserInformation(token: String): Response<UserResponse>{
        return userServiceAPI.getUserInformation(token)
    }

    suspend fun addComment(token : String,text_comment : String,idgame : Int): Response<Message> {
        return userServiceAPI.addComment(token,text_comment,idgame)
    }
}