package com.dlha.addictinggame.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dlha.addictinggame.data.Repository
import com.dlha.addictinggame.model.GameItem
import com.dlha.addictinggame.model.Games
import com.dlha.addictinggame.model.User
import com.dlha.addictinggame.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {

    val newGamesResponse: MutableLiveData<NetworkResult<List<GameItem>>> = MutableLiveData()

    fun getNewGames() = viewModelScope.launch {
            getNewGamesSafeCall()
    }

    private suspend fun getNewGamesSafeCall() {
        newGamesResponse.value = NetworkResult.Loading()
        try{
            val response = repository.remote.getListNewGames()
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
}