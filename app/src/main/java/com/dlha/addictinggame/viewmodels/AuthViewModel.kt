package com.dlha.addictinggame.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dlha.addictinggame.data.DataStoreRepository
import com.dlha.addictinggame.data.Repository
import com.dlha.addictinggame.model.Message
import com.dlha.addictinggame.model.User
import com.dlha.addictinggame.model.UserResponse
import com.dlha.addictinggame.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {

    /* Retrofit */
    val userRegisterResponse: MutableLiveData<NetworkResult<Message>> = MutableLiveData()
    val userLoginResponse: MutableLiveData<NetworkResult<UserResponse>> = MutableLiveData()


    //DataStore
    val userInfoResponse : MutableLiveData<NetworkResult<User>> = MutableLiveData()
    val userToken = dataStoreRepository.readAuthToken.asLiveData()

    val user : MutableLiveData<User> = MutableLiveData()


    fun userRegister(email: String, username: String, password: String) = viewModelScope.launch {
        getUserRegisterSafeCall(email, username, password)
    }

    fun userLogin(username: String, password: String) = viewModelScope.launch {
        getUserLoginSafeCall(username, password)
    }

    fun getUserInfo() = viewModelScope.launch {
        getUserInfoSafeCall()
    }

    private suspend fun getUserRegisterSafeCall(email: String, username: String, password: String) {
        userRegisterResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.userRegister(email, username, password)
            userRegisterResponse.value = handleUserRegisterResponse(response)
        } catch (e: Exception) {
            userRegisterResponse.value = NetworkResult.Error(e.message)
        }
    }

    private suspend fun getUserLoginSafeCall(username: String, password: String) {
        userLoginResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.userLogin(username, password)
            userLoginResponse.value = handleUserLoginResponse(response)
        } catch (e: Exception) {
            userLoginResponse.value = NetworkResult.Error("User not found")
        }
    }


    private suspend fun getUserInfoSafeCall() {
        userInfoResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.getUserInfo(dataStoreRepository.getAuthToken()!!)
            userInfoResponse.value = handleUserInfoResponse(response)
        } catch (e: Exception) {
            userInfoResponse.value = NetworkResult.Error("Token error")
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
                val message = response.body()
                user.value = message!!
                return NetworkResult.Success(message)
            }
            else -> NetworkResult.Error(response.message().toString())
        }
    }


    private fun handleUserRegisterResponse(response: Response<Message>): NetworkResult<Message> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout.")
            }
            response.body()!!.message == "Already Username" -> {
                Log.d(
                    "RegisterViewModel",
                    "handleUserRegisterResponse: " + response.body()!!.message.toString()
                )
                return NetworkResult.Error("Already Username.")
            }
            response.isSuccessful -> {
                val message = response.body()
                NetworkResult.Success(message!!)
            }
            else -> NetworkResult.Error(response.message().toString())
        }
    }

    private fun handleUserLoginResponse(response: Response<UserResponse>): NetworkResult<UserResponse> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout.")
            }
            response.body()!!.token.isEmpty() -> {
                NetworkResult.Error("User not found!")
            }
            response.isSuccessful -> {
                val user = response.body()
                saveAuthKey(user!!.token)
                return NetworkResult.Success(user)
            }
            else -> NetworkResult.Error(response.message().toString())
        }
    }

    fun clearAuthKey() = viewModelScope.launch(Dispatchers.IO) {
        Log.d("Profile", "clearAuthKey: ")
        dataStoreRepository.deleteAuthToken()
    }

    fun saveAuthKey(token: String) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveAuthToken(token)
        Log.d("Profile", "saveAuthKey: " + dataStoreRepository.getAuthToken())
    }

    suspend fun isAuthKeyNull() : Boolean {
        return dataStoreRepository.getAuthToken() == null
    }
}