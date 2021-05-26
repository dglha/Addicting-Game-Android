package com.dlha.addictinggame.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlha.addictinggame.ReviewBottomSheet
import com.dlha.addictinggame.adapter.CommentAdapter
import com.dlha.addictinggame.databinding.ActivityReviewsBinding
import com.dlha.addictinggame.utils.NetworkResult
import com.dlha.addictinggame.viewmodels.CommentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewsActivity : AppCompatActivity() {

    private var _binding : ActivityReviewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var commentViewModel: CommentViewModel

    private val mAdapter by lazy { CommentAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityReviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        commentViewModel = ViewModelProvider(this).get(CommentViewModel::class.java)

        setSupportActionBar(binding.reviewToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupRecyclerView()
        val idgame = intent.getIntExtra("idgame",0)
        readAPI(idgame)

        binding.reviewOpenBottomSheetFab.setOnClickListener {
            val bottomSheet = ReviewBottomSheet()
            val bundle = Bundle()
            bundle.putInt("idgame",idgame)
            bottomSheet.arguments = bundle
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            bottomSheet.check.observe(this@ReviewsActivity) {
                if(it==true) {
                    readAPI(idgame)
                }
            }
        }
    }

    private fun readAPI(idgame : Int) {
        lifecycleScope.launch {
            commentViewModel.getListCommentInGame(idgame)
            commentViewModel.listCommentResponse.observe(this@ReviewsActivity) { response ->
                when(response) {
                    is NetworkResult.Success -> {
                        Log.d("ReviewsActivity", "readApi: success")
                        hideShimmerEffect()
                        response.data?.let {
                            mAdapter.setData(it)
                            if(it.count() == 1) {
                                binding.reviewReviewCountTextView.text = it.count().toString()+" Review"
                            } else {
                                binding.reviewReviewCountTextView.text = it.count().toString()+" Reviews"
                            }
                        }
                    }
                    is NetworkResult.Loading -> {
                        showShimmerEffect()
                    }
                    is NetworkResult.Error -> {
                        hideShimmerEffect()
                        Toast.makeText(this@ReviewsActivity, response.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun setupRecyclerView() {
        binding.commentRecyclerView.adapter = mAdapter
        binding.commentRecyclerView.layoutManager = LinearLayoutManager(this)
        showShimmerEffect()
    }
    private fun showShimmerEffect() {
        binding.commentRecyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.commentRecyclerView.hideShimmer()
    }

    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return super.onSupportNavigateUp()
    }

}