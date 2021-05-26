package com.dlha.addictinggame.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dlha.addictinggame.data.DataStoreRepository
import com.dlha.addictinggame.data.Repository
import com.dlha.addictinggame.model.GameItem
import com.dlha.addictinggame.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SearchViewModel@Inject constructor(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {

    //DataStore
    val userToken = dataStoreRepository.readAuthToken.asLiveData()

    val listSearchGameResponse : MutableLiveData<NetworkResult<List<GameItem>>> = MutableLiveData()

    fun searchGame(token : String,namegame : String) {
        viewModelScope.launch {
            searchGameSafeCall(token,namegame)
        }
    }

    private suspend fun searchGameSafeCall(token : String,namegame: String) {
        listSearchGameResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.getListSearhGame(token,namegame)
            listSearchGameResponse.value = handleSearchGameResponse(response)
        } catch (e : Exception) {
            listSearchGameResponse.value = NetworkResult.Error(e.message.toString())
        }
    }

    private fun handleSearchGameResponse(response: Response<List<GameItem>>) : NetworkResult<List<GameItem>> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout.")
            }
            response.body()?.isNullOrEmpty()!! -> {
                NetworkResult.Error("User not found!")
            }
            response.isSuccessful -> {
                val data = response.body()!!
                return NetworkResult.Success(data)
            }
            else -> NetworkResult.Error(response.message().toString())
        }
    }
}