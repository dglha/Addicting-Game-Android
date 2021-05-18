package com.dlha.addictinggame.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dlha.addictinggame.data.Repository
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

    /* Retrofit */
    val userLoginResponse: MutableLiveData<NetworkResult<User>> = MutableLiveData()

    fun userLogin(username: String, password: String) = viewModelScope.launch {
        getUserLoginSafeCall(username, password)
    }

    private suspend fun getUserLoginSafeCall(username: String, password: String) {
        userLoginResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.userLogin(username, password)
            userLoginResponse.value = handleUserLoginResponse(response)
        } catch (e: Exception){
            userLoginResponse.value = NetworkResult.Error("User not found")
        }
    }

    private fun handleUserLoginResponse(response: Response<User>): NetworkResult<User> {
        return when{
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout.")
            }
            response.body()!!.token.isEmpty() -> {
                NetworkResult.Error("User not found!")
            }
            response.isSuccessful -> {
                val user = response.body()
                NetworkResult.Success(user!!)
            }
            else -> NetworkResult.Error(response.message().toString())
        }
    }
}