package com.dlha.addictinggame.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dlha.addictinggame.data.DataStoreRepository
import com.dlha.addictinggame.data.Repository
import com.dlha.addictinggame.model.GameItem
import com.dlha.addictinggame.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application){

    val gameLibrary: MutableLiveData<NetworkResult<List<GameItem>>> = MutableLiveData()

    fun getLibrary() = viewModelScope.launch {
        getLibrarySafeCall()
    }

    private suspend fun getLibrarySafeCall() {
        gameLibrary.value = NetworkResult.Loading()
        try {
            val response = repository.remote.getListGameaHaving(dataStoreRepository.getAuthToken()!!)
            gameLibrary.value = handleLibrary(response)
        } catch (e: Exception) {
            gameLibrary.value = NetworkResult.Error(e.message)
        }
    }

    private fun handleLibrary(response: Response<List<GameItem>>): NetworkResult<List<GameItem>> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout.")
            }
            response.body().isNullOrEmpty() -> {
                NetworkResult.Error("No Games found!")
            }
            response.isSuccessful -> {
                Log.d("Library", "handleLibrary: "+ response.body())
                NetworkResult.Success(response.body()!!)
            }
            else -> return NetworkResult.Error(response.message().toString())
        }
    }

}