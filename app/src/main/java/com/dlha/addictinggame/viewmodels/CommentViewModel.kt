package com.dlha.addictinggame.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dlha.addictinggame.data.DataStoreRepository
import com.dlha.addictinggame.data.Repository
import com.dlha.addictinggame.model.Comment
import com.dlha.addictinggame.model.Message
import com.dlha.addictinggame.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {

    val userToken = dataStoreRepository.readAuthToken.asLiveData()

    val listCommentResponse: MutableLiveData<NetworkResult<List<Comment>>> = MutableLiveData()
    val messageCommentResponse: MutableLiveData<NetworkResult<Message>> = MutableLiveData()

    fun getListCommentInGame(idgame: Int) {
        viewModelScope.launch {
            getListCommentInGameSafeCall(idgame)
        }
    }

    fun addComment(token: String, text_comment: String, idgame: Int) {
        viewModelScope.launch {
            addCommentSafeCall(token, text_comment, idgame)
        }
    }


    private suspend fun getListCommentInGameSafeCall(idgame: Int) {
        listCommentResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.getListCommentInGame(idgame)
            listCommentResponse.value = handleListCommentResponse(response)
        } catch (e: Exception) {
            listCommentResponse.value = NetworkResult.Error(e.message)
        }
    }

    private fun handleListCommentResponse(response: Response<List<Comment>>): NetworkResult<List<Comment>>? {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout.")
            }
            response.body()!!.isNullOrEmpty() -> {
                NetworkResult.Error("No comment found!")
            }
            response.isSuccessful -> {
                val listComment = response.body()
                return NetworkResult.Success(listComment!!)
            }
            else -> return NetworkResult.Error(response.message().toString())
        }
    }

    private suspend fun addCommentSafeCall(token: String, text_comment: String, idgame: Int) {
        messageCommentResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.addComment(token, text_comment, idgame)
            messageCommentResponse.value = handleMessageCommentResponse(response)
        } catch (e: Exception) {
            messageCommentResponse.value = NetworkResult.Error(e.message)
        }
    }

    private fun handleMessageCommentResponse(response: Response<Message>): NetworkResult<Message>? {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout.")
            }
            response.body()!!.message == "expired" -> {
                NetworkResult.Error("Token expired")
            }
            response.body()!!.message == "Token does not exist" -> {
                NetworkResult.Error("Token does not exist")
            }
            response.isSuccessful -> {
                val message = response.body()
                return NetworkResult.Success(message!!)
            }
            else -> return NetworkResult.Error(response.message().toString())
        }
    }
}