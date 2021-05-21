package com.dlha.addictinggame.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dlha.addictinggame.data.DataStoreRepository
import com.dlha.addictinggame.data.Repository
import com.dlha.addictinggame.model.GameItem
import com.dlha.addictinggame.model.Message
import com.dlha.addictinggame.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application){

    //DataStore
    val userFavoriteResponse: MutableLiveData<NetworkResult<List<GameItem>>> = MutableLiveData()
    val userAddFavoriteResponse: MutableLiveData<NetworkResult<Message>> = MutableLiveData()
    val userUnFavoriteResponse: MutableLiveData<NetworkResult<Message>> = MutableLiveData()

    fun getListFavorite() = viewModelScope.launch {
        getListFavoriteSafeCall()
    }

    fun unFavoriteGameHaveId(id: Int) = viewModelScope.launch {
//        unFavoriteGameHaveIdSafeCall(id)
        repository.remote.unFavoriteGameHaveId(dataStoreRepository.getAuthToken()!!, id)
    }

    fun addFavoriteGameHaveId(id: Int) = viewModelScope.launch {
//        repository.remote.unFavoriteGameHaveId(dataStoreRepository.getAuthToken()!!, id)
        addFavoriteGameHaveIdSafeCall(id)
    }

    private suspend fun addFavoriteGameHaveIdSafeCall(id: Int) {
        userFavoriteResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.addFavoriteGameHaveId(dataStoreRepository.getAuthToken()!!, id)
            userAddFavoriteResponse.value = handleUserAddFavoriteResponse(response)
        } catch (e: Exception){
            userFavoriteResponse.value = NetworkResult.Error(e.message)
            Log.d("ListFavoGame", "userAddFavoriteSafeCall: error: " +e.message)
        }
    }

    private suspend fun getListFavoriteSafeCall() {
        userFavoriteResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.getUserFavoriteListGames(dataStoreRepository.getAuthToken()!!)
            userFavoriteResponse.value = handleUserFavoriteResponse(response)
        } catch (e: Exception){
            userFavoriteResponse.value = NetworkResult.Error(e.message)
            Log.d("ListFavoGame", "getListFavoriteSafeCall: error: " +e.message)
        }
    }

    private suspend fun unFavoriteGameHaveIdSafeCall(id: Int) {
        userFavoriteResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.unFavoriteGameHaveId(dataStoreRepository.getAuthToken()!!, id)
            userUnFavoriteResponse.value = handleUserUnFavoriteResponse(response)
        } catch (e: Exception){

        }
    }

    private fun handleUserUnFavoriteResponse(response: Response<Message>): NetworkResult<Message> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout.")
            }
            response.isSuccessful -> {
                NetworkResult.Success(response.body()!!)
            }
            else -> return NetworkResult.Error(response.message().toString())
        }
    }

    private fun handleUserFavoriteResponse(response: Response<List<GameItem>>): NetworkResult<List<GameItem>> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout.")
            }
            response.body().isNullOrEmpty() -> {
                NetworkResult.Error("No Games found!")
            }
            response.isSuccessful -> {
                Log.d("Favo", "handleUserFavoriteResponse: "+ response.body())
                NetworkResult.Success(response.body()!!)
            }
            else -> return NetworkResult.Error(response.message().toString())
        }
    }

    private fun handleUserAddFavoriteResponse(response: Response<Message>): NetworkResult<Message>? {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout.")
            }
            response.isSuccessful -> {
                NetworkResult.Success(response.body()!!)
            }
            else -> return NetworkResult.Error(response.message().toString())
        }
    }

}