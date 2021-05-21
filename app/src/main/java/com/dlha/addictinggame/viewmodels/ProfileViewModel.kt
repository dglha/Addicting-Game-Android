package com.dlha.addictinggame.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dlha.addictinggame.data.DataStoreRepository
import com.dlha.addictinggame.data.Repository
import com.dlha.addictinggame.model.User
import com.dlha.addictinggame.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {

    val userInfoResponse : MutableLiveData<NetworkResult<User>> = MutableLiveData()

    //DataStore
    val userToken = dataStoreRepository.readAuthToken.asLiveData()

    fun getUserInfo(token: String) = viewModelScope.launch {
        getUserInfoSafeCall(token)
    }

    private suspend fun getUserInfoSafeCall(token: String) {
        userInfoResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.getUserInfo(token)
            userInfoResponse.value = handleUserInfoResponse(response)
        } catch (e: Exception) {
            userInfoResponse.value = NetworkResult.Error(token)
        }
    }

    private fun handleUserInfoResponse(response: Response<User>): NetworkResult<User> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout.")
            }
            response.body()?.username.isNullOrEmpty() -> {
                NetworkResult.Error("User not found!")
            }
            response.isSuccessful -> {
                val message = response.body()!!
                return NetworkResult.Success(message)
            }
            else -> NetworkResult.Error(response.message().toString())
        }
    }

    fun clearAuthKey() = viewModelScope.launch(Dispatchers.IO) {
        Log.d("Profile", "clearAuthKey: ")
        dataStoreRepository.deleteAuthToken()
    }
}