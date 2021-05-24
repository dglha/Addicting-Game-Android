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

    suspend fun getListNewGames(token : String) : Response<List<GameItem>>{
        return appServiceAPI.getListNewGames(token)
    }

    suspend fun getListSaleGames(token : String): Response<List<GameItem>> {
        return appServiceAPI.getListSaleGame(token)
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

    suspend fun getUserFavoriteListGames(token: String): Response<List<GameItem>>{
        return userServiceAPI.getUserFavoriteListGames(token)
    }

    suspend fun unFavoriteGameHaveId(token: String, id: Int): Response<Message>{
        return userServiceAPI.unFavoriteGameHaveId(token, id)
    }

    suspend fun addFavoriteGameHaveId(token: String, id: Int): Response<Message>{
        return userServiceAPI.addFavoriteGameHaveId(token, id)
    }

    suspend fun addComment(token : String,text_comment : String,idgame : Int): Response<Message> {
        return userServiceAPI.addComment(token,text_comment,idgame)
    }

    suspend fun getGamesInCart(token : String) : Response<List<GameItem>> {
        return userServiceAPI.listGameInCart(token)
    }

    suspend fun buyGame(token : String,idgame : Int) : Response<Message> {
        return userServiceAPI.buyGame(token, idgame)
    }

    suspend fun removeGameCart(token : String,idgame : Int) : Response<Message> {
        return userServiceAPI.removeGameCart(token, idgame)
    }

    suspend fun getListGameaHaving(token: String): Response<List<GameItem>>{
        return userServiceAPI.getListGameHaving(token)
    }

    suspend fun checkout(token : String,listID : String) : Response<Message> {
        return userServiceAPI.checkout(token, listID)
    }
}