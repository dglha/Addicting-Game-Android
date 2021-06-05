package com.dlha.addictinggame.ui.activities

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlha.addictinggame.R
import com.dlha.addictinggame.adapter.NewGameModuleAdapter
import com.dlha.addictinggame.adapter.PlayGameActivity
import com.dlha.addictinggame.databinding.ActivityDetailsBinding
import com.dlha.addictinggame.model.GameItem
import com.dlha.addictinggame.utils.NetworkResult
import com.dlha.addictinggame.viewmodels.CartViewModel
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
    private val cartViewModel: CartViewModel by viewModels()

    private lateinit var gameItem: GameItem

    private val mAdapter: NewGameModuleAdapter by lazy { NewGameModuleAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityDetailsBinding.inflate(layoutInflater)
        Log.d("NavToDetails", "inflate")
        setContentView(binding.root)

        setSupportActionBar(binding.detailToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        gameItem = if (args.gameItem == null)
            intent.getParcelableExtra<GameItem>("item")!!
        else args.gameItem!!


        Log.d("NavToDetails", "game: " + gameItem!!.name)

        setupCarouselItem(gameItem)
        setUpContentView(gameItem)


        checkBuy(gameItem)
        checkFavorite(gameItem)

        binding.commentCardCardView.setOnClickListener {
            startActivity(Intent(this, ReviewsActivity::class.java).putExtra("idgame", gameItem.id))
        }

        binding.detailsAddToFavoriteButton.setOnClickListener {
            addToFavorite(gameItem)
        }

        binding.detailAddToCartFab.setOnClickListener {
            cartViewModel.userToken.observe(this@DetailsActivity) {
                if (it == "null") {
                    Toast.makeText(this@DetailsActivity, "Please Login!!!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    addGameToCartAPI(it, gameItem.id)
                }
            }
        }


        setupRecyclerView()

        mainViewModel.userToken.observe(this@DetailsActivity) {
            if (it != null) {
                readYouMayLikeApi(gameItem.categoryId, gameItem.id, it)
            } else {
                readYouMayLikeApi(gameItem.categoryId, gameItem.id, "")
            }
        }


    }

    private fun checkBuy(gameItem: GameItem) {
        if (gameItem.isBuy > 0) {
            binding.detailDownloadFab.visibility = View.VISIBLE
            binding.playNowButton.visibility = View.VISIBLE
            binding.detailAddToCartFab.visibility = View.GONE

            binding.playNowButton.setOnClickListener {
                startActivity(
                    Intent(
                        this@DetailsActivity,
                        PlayGameActivity::class.java
                    ).putExtra("webgame", gameItem.link)
                )
            }

            binding.detailDownloadFab.setOnClickListener {
                val url = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"

                val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
                val title: String = URLUtil.guessFileName(url, null, null)
                request.setTitle(title)
                request.setDescription("Download...")
                val cookie = CookieManager.getInstance().getCookie(url)
                request.addRequestHeader("cookie", cookie)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title)

                val downloadManager: DownloadManager =
                    getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.enqueue(request)

                Toast.makeText(this@DetailsActivity, "Download Start", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkFavorite(gameItem: GameItem) {
        if (gameItem.isFavorite > 0) {
            binding.detailsAddToFavoriteButton.setIconResource(R.drawable.ic_heart)
            binding.detailsAddToFavoriteButton.setIconTintResource(R.color.red)
        }
    }

    private fun setupRecyclerView() {
        binding.detailsRecyclerView.adapter = mAdapter
        binding.detailsRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        showShimmerEffect()
    }

    private fun addGameToCartAPI(token: String, gameId: Int) {
        lifecycleScope.launch {
            cartViewModel.buyGame(token, gameId)
            cartViewModel.messageResponse.observe(this@DetailsActivity) { response ->
                when (response) {
                    is NetworkResult.Loading -> {

                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(this@DetailsActivity, response.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    is NetworkResult.Success -> {
                        Toast.makeText(
                            this@DetailsActivity,
                            "Mua game thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun readYouMayLikeApi(cateId: Int, gameId: Int, token: String) {
        lifecycleScope.launch {
            mainViewModel.getGamesInCategory(cateId, token)
            mainViewModel.gamesInCategoryResponse.observe(this@DetailsActivity) { response ->
                when (response) {
                    is NetworkResult.Loading -> {

                        showShimmerEffect()
                    }
                    is NetworkResult.Error -> {
                        hideShimmerEffect()
                        Toast.makeText(
                            this@DetailsActivity,
                            response.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is NetworkResult.Success -> {
                        Log.d("SUCCESS", "ERR")
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
            favoriteViewModel.userAddFavoriteResponse.observe(this@DetailsActivity) { response ->
                when (response) {
                    is NetworkResult.Loading -> {
                        Toast.makeText(this@DetailsActivity, "Waiting...", Toast.LENGTH_SHORT)
                            .show()
                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(this@DetailsActivity, response.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    is NetworkResult.Success -> {
                        Toast.makeText(
                            this@DetailsActivity,
                            "Added ${gameItem.name} to favorite!",
                            Toast.LENGTH_SHORT
                        ).show()
                        changeFavoriteButtonColor()
                    }
                }
            }
        }
    }

    private fun changeFavoriteButtonColor() {
        binding.detailsAddToFavoriteButton.setIconResource(R.drawable.ic_heart)
        binding.detailsAddToFavoriteButton.iconTint =
            ContextCompat.getColorStateList(this, R.color.orangeStrong)
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


    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share_detail_menu) {

            val intent = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Checkout this game: ${gameItem.name}")
                putExtra(Intent.EXTRA_TEXT, "Checkout this game: ${gameItem.name}\nhttps://whynotaddicting.000webhostapp.com/game/${gameItem.id}")
                putExtra(Intent.EXTRA_TITLE, gameItem.name)
            }, "Share this game!")
            startActivity(intent)
            return true
        } else return super.onOptionsItemSelected(item);
    }
}