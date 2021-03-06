package com.dlha.addictinggame.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dlha.addictinggame.data.DataStoreRepository
import com.dlha.addictinggame.data.Repository
import com.dlha.addictinggame.model.Category
import com.dlha.addictinggame.model.GameItem
import com.dlha.addictinggame.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
): AndroidViewModel(application) {

    val userToken = dataStoreRepository.readAuthToken.asLiveData()

    val newGamesResponse: MutableLiveData<NetworkResult<List<GameItem>>> = MutableLiveData()
    val saleGameResponse: MutableLiveData<NetworkResult<List<GameItem>>> = MutableLiveData()
    val categoryResponse: MutableLiveData<NetworkResult<List<Category>>> = MutableLiveData()
    val gamesInCategoryResponse: MutableLiveData<NetworkResult<List<GameItem>>> = MutableLiveData()

    fun getNewGames(token : String) = viewModelScope.launch {
        getNewGamesSafeCall(token)
    }
    fun getSaleGames(token: String) = viewModelScope.launch {
        getSaleGamesSafeCall(token)
    }
    fun getCategory() = viewModelScope.launch {
        getCategoriesSafeCall()
    }
    fun getGamesInCategory(idcategory : Int,token: String) = viewModelScope.launch {
        getGamesInCategorySafeCall(idcategory,token)
    }

    private suspend fun getNewGamesSafeCall(token : String) {
        newGamesResponse.value = NetworkResult.Loading()
        try{
            val response = repository.remote.getListNewGames(token)
            newGamesResponse.value = handleListNewGamesResponse(response)
        } catch (e: Exception){
            newGamesResponse.value = NetworkResult.Error(e.message)
            Log.d("ListNewGame", "getNewGamesSafeCall: error: " +e.message)
        }
    }
    private fun handleListNewGamesResponse(response: Response<List<GameItem>>): NetworkResult<List<GameItem>>? {
        return when{
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout.")
            }
            response.body()!!.isNullOrEmpty() -> {
                NetworkResult.Error("No Game found!")
            }
            response.isSuccessful -> {
                val newGames = response.body()
                return NetworkResult.Success(newGames!!)
            }
            else -> return NetworkResult.Error(response.message().toString())
        }
    }

    private suspend fun getSaleGamesSafeCall(token : String) {
        saleGameResponse.value = NetworkResult.Loading()
        try{
            val response = repository.remote.getListSaleGames(token)
            saleGameResponse.value = handleListSaleGameResponse(response)
        } catch (e: Exception){
            saleGameResponse.value = NetworkResult.Error(e.message)
            Log.d("ListSaleGame", "getSaleGamesSafeCall: error: " +e.message)
        }
    }
    private fun handleListSaleGameResponse(response: Response<List<GameItem>>): NetworkResult<List<GameItem>>? {
        return when{
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout.")
            }
            response.body()!!.isNullOrEmpty() -> {
                NetworkResult.Error("No Game found!")
            }
            response.isSuccessful -> {
                val saleGames = response.body()
                return NetworkResult.Success(saleGames!!)
            }
            else -> return NetworkResult.Error(response.message().toString())
        }
    }

    private suspend fun getCategoriesSafeCall() {
        categoryResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.getListCategories()
            categoryResponse.value =  handleListCategoriesResponse(response)
        }catch (e: Exception) {
            categoryResponse.value = NetworkResult.Error(e.message)
            Log.d("ListCategories", "getCategoriesSafeCall: error: " +e.message)
        }
    }
    private fun handleListCategoriesResponse(response: Response<List<Category>>): NetworkResult<List<Category>>? {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout.")
            }
            response.body()!!.isNullOrEmpty() -> {
                NetworkResult.Error("No Category found!")
            }
            response.isSuccessful -> {
                val categories = response.body()
                return NetworkResult.Success(categories!!)
            }
            else -> return  NetworkResult.Error(response.message().toString())
        }
    }

    private suspend fun getGamesInCategorySafeCall(idcategory : Int,token: String) {
        gamesInCategoryResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.getListGameInCategory(idcategory,token)
            gamesInCategoryResponse.value = handleListGamesInCategoryResponse(response)
        } catch (e : Exception) {
            gamesInCategoryResponse.value = NetworkResult.Error(e.message)
            Log.d("ListGameInCategory", "getNewGamesSafeCall: error: " +e.message)
        }
    }
    private fun handleListGamesInCategoryResponse(response: Response<List<GameItem>>): NetworkResult<List<GameItem>> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout.")
            }
            response.body()!!.isNullOrEmpty() -> {
                NetworkResult.Error("No Games in this Category found!")
            }
            response.isSuccessful -> {
                val games = response.body()
                Log.d("COUNT",games?.count().toString())
                return NetworkResult.Success(games!!)
            }
            else -> return  NetworkResult.Error(response.message().toString())
        }
    }
}