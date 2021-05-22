package com.dlha.addictinggame.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlha.addictinggame.R
import com.dlha.addictinggame.ReviewsActivity
import com.dlha.addictinggame.adapter.NewGameModuleAdapter
import com.dlha.addictinggame.databinding.ActivityDetailsBinding
import com.dlha.addictinggame.model.GameItem
import com.dlha.addictinggame.utils.NetworkResult
import com.dlha.addictinggame.viewmodels.FavoriteViewModel
import com.dlha.addictinggame.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import org.imaginativeworld.whynotimagecarousel.CarouselType

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private var _binding: ActivityDetailsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailsActivityArgs>()

    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    private val mAdapter: NewGameModuleAdapter by lazy { NewGameModuleAdapter(this) }

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

        binding.detailsAddToFavoriteButton.setOnClickListener {
            addToFavorite(gameItem)
        }
        setupRecyclerView()
        readYouMayLikeApi(gameItem.categoryId, gameItem.id)


    }

    private fun setupRecyclerView() {
        binding.detailsRecyclerView.adapter = mAdapter
        binding.detailsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        showShimmerEffect()
    }

    private fun readYouMayLikeApi(cateId: Int, gameId: Int) {
        lifecycleScope.launch {
            mainViewModel.getGamesInCategory(cateId)
            mainViewModel.gamesInCategoryResponse.observe(this@DetailsActivity) { response ->
                when(response) {
                    is NetworkResult.Loading -> {

                        showShimmerEffect()
                    }
                    is NetworkResult.Error -> {
                        hideShimmerEffect()
                        Toast.makeText(this@DetailsActivity,response.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Success -> {
                        Log.d("SUCCESS","ERR")
                        Log.d("SUCCESS", "readAPI: " + response.data.toString())
                        response.data?.let {

                            mAdapter.setData(it.filter { game -> game.id != gameId })
                        }
                        hideShimmerEffect()
                    }
                }
            }
        }
    }

    private fun hideShimmerEffect() {
        binding.detailsRecyclerView.hideShimmer()
    }

    private fun showShimmerEffect() {
        binding.detailsRecyclerView.showShimmer()
    }

    private fun addToFavorite(gameItem: GameItem) {
        lifecycleScope.launch {
            favoriteViewModel.addFavoriteGameHaveId(gameItem.id)
            favoriteViewModel.userAddFavoriteResponse.observe(this@DetailsActivity, { response ->
                when (response) {
                    is NetworkResult.Loading -> {
                        Toast.makeText(this@DetailsActivity, "Waiting...", Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(this@DetailsActivity, response.message, Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Success -> {
                        Toast.makeText(this@DetailsActivity, "Added ${gameItem.name} to favorite!", Toast.LENGTH_SHORT).show()
                        changeFavoriteButtonColor()
                    }
                }
            })
        }
    }

    private fun changeFavoriteButtonColor() {
        binding.detailsAddToFavoriteButton.setIconResource(R.drawable.ic_heart)
        binding.detailsAddToFavoriteButton.iconTint =
            ContextCompat.getColorStateList(this, R.color.orangeRed)
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