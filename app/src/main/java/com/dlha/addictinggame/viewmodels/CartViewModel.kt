package com.dlha.addictinggame.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
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
class CartViewModel  @Inject constructor(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {

    val userToken = dataStoreRepository.readAuthToken.asLiveData()

    val listGameInCartResponse : MutableLiveData<NetworkResult<List<GameItem>>> = MutableLiveData()
    val messageResponse : MutableLiveData<NetworkResult<Message>> = MutableLiveData()

    fun getListGameInCart(token : String) {
        viewModelScope.launch {
            getListGameInCartSafeCall(token)
        }
    }
    fun buyGame(token: String,idgame : Int) {
        viewModelScope.launch {
            buyGameSafeCall(token, idgame)
        }
    }
    fun removeGameFromCart(idgame: Int){
        viewModelScope.launch {
            removeGameFromCartSafeCall(idgame)
        }
    }
    fun checkout(token : String,listIDGame : String) {
        viewModelScope.launch {
            checkoutSafeCall(token,listIDGame)
        }
    }


    suspend fun getListGameInCartSafeCall(token : String) {
        listGameInCartResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.getGamesInCart(token)
            listGameInCartResponse.value = handleListGameInCart(response)
        } catch (e : Exception) {
            listGameInCartResponse.value = NetworkResult.Error(e.message)
        }
    }
    fun handleListGameInCart(response : Response<List<GameItem>>) : NetworkResult<List<GameItem>> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout.")
            }
            response.body()!!.isNullOrEmpty() -> {
                NetworkResult.Error("There are no games in the cart!")
            }
            response.isSuccessful -> {
                val listGame = response.body()
                return NetworkResult.Success(listGame!!)
            }
            else -> return NetworkResult.Error(response.message().toString())
        }
    }
    suspend fun buyGameSafeCall(token: String,idgame: Int) {
        messageResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.buyGame(token,idgame)
            messageResponse.value = handleBuyGame(response)
        } catch (e : Exception) {
            messageResponse.value = NetworkResult.Error(e.message)
        }
    }
    fun handleBuyGame(response: Response<Message>) : NetworkResult<Message> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout.")
            }
            response.body()?.message.isNullOrEmpty() -> {
                NetworkResult.Error("Game not found!!!")
            }
            response.body()?.message == "already" -> {
                NetworkResult.Error("The game is already in your cart")
            }
            response.isSuccessful -> {
                val message = response.body()!!
                return NetworkResult.Success(message)
            }
            else -> NetworkResult.Error(response.message().toString())
        }
    }

    private suspend fun removeGameFromCartSafeCall(idgame: Int) {
        messageResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.removeGameCart(dataStoreRepository.getAuthToken()!!,  idgame)
            messageResponse.value = handleRemoveGameFromCart(response)
        } catch (e : Exception) {
            messageResponse.value = NetworkResult.Error(e.message)
        }
    }

    private fun handleRemoveGameFromCart(response: Response<Message>): NetworkResult<Message> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout.")
            }
            response.isSuccessful -> {
                val message = response.body()!!
                return NetworkResult.Success(message)
            }
            else -> NetworkResult.Error(response.message().toString())
        }
    }

    private suspend fun checkoutSafeCall(token: String,listIDGame: String) {
        messageResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.checkout(token, listIDGame)
            messageResponse.value = handleCheckout(response)
        } catch (e : Exception) {
            messageResponse.value = NetworkResult.Error(e.message)
        }
    }
    private fun handleCheckout(response: Response<Message>): NetworkResult<Message> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout.")
            }
            response.isSuccessful -> {
                val message = response.body()!!
                return NetworkResult.Success(message)
            }
            else -> NetworkResult.Error(response.message().toString())
        }
    }
}