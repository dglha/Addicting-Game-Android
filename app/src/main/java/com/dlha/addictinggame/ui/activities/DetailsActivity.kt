package com.dlha.addictinggame.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.navigation.navArgs
import com.dlha.addictinggame.R
import com.dlha.addictinggame.ReviewsActivity
import com.dlha.addictinggame.databinding.ActivityDetailsBinding
import com.dlha.addictinggame.model.GameItem
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import org.imaginativeworld.whynotimagecarousel.CarouselType

class DetailsActivity : AppCompatActivity() {

    private var _binding : ActivityDetailsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailsActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityDetailsBinding.inflate(layoutInflater)
        Log.d("NavToDetails", "inflate")
        setContentView(binding.root)

        setSupportActionBar(binding.detailToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imageList = mutableListOf<CarouselItem>()

        val gameItem = args.gameItem

        Log.d("NavToDetails", "game: " + gameItem.name)

//        imageList.add(CarouselItem(gameItem.image))
        imageList.add(CarouselItem(R.drawable.fd1dda13059050ea67316cdc29198af8))
        imageList.add(CarouselItem(R.drawable.cyberpunk20770_olto))
        imageList.add(CarouselItem(R.drawable.fd1dda13059050ea67316cdc29198af8))

        setUpContentView(gameItem)

        binding.detailGameImagesImageCarousel.carouselType = CarouselType.SHOWCASE
        binding.detailGameImagesImageCarousel.addData(imageList)

        binding.commentCardCardView.setOnClickListener {
            startActivity(Intent(this, ReviewsActivity::class.java))
        }


    }

    private fun setUpContentView(gameItem: GameItem) {
        binding.detailToolbarTitle.text = gameItem.name
        binding.detailGameDeveloperTextView.text = gameItem.developer
        binding.detailGameDescriptionTextView.text = gameItem.detail
        binding.detailGameCoinTextView.text = gameItem.coin
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_activity_menu, menu)
        return true
    }
}