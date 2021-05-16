package com.dlha.addictinggame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dlha.addictinggame.databinding.ActivityReviewsBinding

class ReviewsActivity : AppCompatActivity() {

    private var _binding : ActivityReviewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityReviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.reviewToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.reviewOpenBottomSheetFab.setOnClickListener {
            val bottomSheet = ReviewBottomSheet()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }
}