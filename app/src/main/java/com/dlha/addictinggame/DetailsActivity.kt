package com.dlha.addictinggame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.dlha.addictinggame.databinding.ActivityDetailsBinding
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import org.imaginativeworld.whynotimagecarousel.CarouselType

class DetailsActivity : AppCompatActivity() {

    private var _binding : ActivityDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.detailToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val imageList = mutableListOf<CarouselItem>()

        imageList.add(CarouselItem(R.drawable.cyberpunk20770_olto))
        imageList.add(CarouselItem(R.drawable.fd1dda13059050ea67316cdc29198af8))
        imageList.add(CarouselItem(R.drawable.cyberpunk20770_olto))
        imageList.add(CarouselItem(R.drawable.fd1dda13059050ea67316cdc29198af8))

        binding.detailGameImagesImageCarousel.carouselType = CarouselType.SHOWCASE
        binding.detailGameImagesImageCarousel.addData(imageList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_activity_menu, menu)
        return true
    }
}