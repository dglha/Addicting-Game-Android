package com.dlha.addictinggame

import android.net.Network
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.dataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.dlha.addictinggame.data.DataStoreRepository
import com.dlha.addictinggame.databinding.FragmentReviewBottomSheetBinding
import com.dlha.addictinggame.utils.NetworkResult
import com.dlha.addictinggame.viewmodels.AuthViewModel
import com.dlha.addictinggame.viewmodels.CommentViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentReviewBottomSheetBinding
    private lateinit var commentViewModel : CommentViewModel

    private val reviewBottomSheet = this

    var check : MutableLiveData<Boolean> = MutableLiveData()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReviewBottomSheetBinding.inflate(layoutInflater)

        commentViewModel = ViewModelProvider(this).get(CommentViewModel::class.java)
        check.value = false

        val idgame =  arguments?.getInt("idgame")
        Log.d("IDGAEEE",idgame.toString())
        binding.reviewSubmitButton.setOnClickListener {
            commentViewModel.userToken.observe(this) {
                if(it=="null") {
                    Toast.makeText(activity,"Please Login!!!",Toast.LENGTH_SHORT).show()
                } else {
                    if (idgame != null) {
                        if(binding.reviewsEditText.text.toString().isNotEmpty()) {
                            addComment(it,binding.reviewsEditText.text.toString(),idgame)
                        }
                    }
                }
            }
        }
        return binding.root
    }
    private fun addComment(token : String,text_comment:String,idgame:Int) {
        lifecycleScope.launch {
            commentViewModel.addComment(token,text_comment,idgame)
            commentViewModel.messageCommentResponse.observe(this@ReviewBottomSheet) { response ->
                when(response) {
                    is NetworkResult.Loading -> {
                        binding.commnetProgressBar.visibility = View.VISIBLE
                        check.value = false
                    }
                    is NetworkResult.Error -> {
                        binding.commnetProgressBar.visibility = View.INVISIBLE
                        Toast.makeText(activity,response.message.toString(),Toast.LENGTH_SHORT).show()
                        check.value = false
                    }
                    is NetworkResult.Success -> {
                        binding.commnetProgressBar.visibility = View.INVISIBLE
                        Log.d("ReviewBottomSheet", "Success -> Message: ${response.data!!.message}")
                        Toast.makeText(activity,response.data!!.message,Toast.LENGTH_SHORT).show()
                        reviewBottomSheet.dismiss()
                        check.value = true
                    }
                }
            }
        }
    }
}