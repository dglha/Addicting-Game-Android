package com.dlha.addictinggame.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.navigation.navArgs
import com.dlha.addictinggame.R
import com.dlha.addictinggame.ReviewsActivity
import com.dlha.addictinggame.databinding.ActivityDetailsBinding
import com.dlha.addictinggame.model.GameItem
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import org.imaginativeworld.whynotimagecarousel.CarouselType

class DetailsActivity : AppCompatActivity() {

    private var _binding: ActivityDetailsBinding? = null
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

        val gameItem = if (args.gameItem==null)
            intent.getParcelableExtra<GameItem>("item")!!
        else args.gameItem

        Log.d("NavToDetails", "game: " + gameItem!!.name)

        setupCarouselItem(gameItem)
        setUpContentView(gameItem)

        binding.commentCardCardView.setOnClickListener {
            startActivity(Intent(this, ReviewsActivity::class.java).putExtra("idgame",gameItem.id))
        }

    }

    private fun setupCarouselItem(gameItem: GameItem) {
        val imageList = mutableListOf<CarouselItem>()
        val part = gameItem.imgtoshow?.split(";")
        imageList.add(CarouselItem(gameItem.image))
        if (part?.count()!! > 0) {
            for (i in 0 until part.count())
                imageList.add(CarouselItem(part[i]))
        }
        binding.detailGameImagesImageCarousel.carouselType = CarouselType.SHOWCASE
        binding.detailGameImagesImageCarousel.addData(imageList)
    }

    private fun setUpContentView(gameItem: GameItem) {
        binding.detailToolbarTitle.text = gameItem.name
        binding.detailGameDeveloperTextView.text = gameItem.developer
        binding.detailGameDescriptionTextView.text = gameItem.detail

        if (gameItem.salePercent.toInt() > 0) {
            val spanBuilder = SpannableStringBuilder(gameItem.coin)
            val strikethroughSpan = StrikethroughSpan()
            spanBuilder.setSpan(
                strikethroughSpan, 0, gameItem.coin.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.detailGameCoinTextView.text = spanBuilder
            binding.coinAfterTextView.text = gameItem.newCoin.toString()
        } else {
            binding.detailGameCoinTextView.text = gameItem.coin
            binding.coinAfterTextView.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_activity_menu, menu)
        return true
    }
}