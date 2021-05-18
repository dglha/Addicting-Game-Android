package com.dlha.addictinggame.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dlha.addictinggame.data.Repository
import com.dlha.addictinggame.model.Message
import com.dlha.addictinggame.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
private val repository: Repository,
application: Application
): AndroidViewModel(application) {

    val userRegisterResponse : MutableLiveData<NetworkResult<Message>> = MutableLiveData()

    fun userRegister(email: String,username : String,password: String) = viewModelScope.launch {
        getUserRegisterSafeCall(email,username,password)
    }
    private  suspend fun getUserRegisterSafeCall(email : String,username: String,password: String) {
        userRegisterResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.userRegister(email,username,password)
            userRegisterResponse.value = handleUserRegisterResponse(response)
        } catch (e: Exception){
            userRegisterResponse.value = NetworkResult.Error("User not found")
        }
    }
    private fun handleUserRegisterResponse(response: Response<Message>) : NetworkResult<Message> {
        return when{
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout.")
            }
            response.body()!!.message == "Already Username" -> {
                NetworkResult.Error("Already Username.")
            }
            response.isSuccessful -> {
                val message = response.body()
                NetworkResult.Success(message!!)
            }
            else -> NetworkResult.Error(response.message().toString())
        }
    }
}