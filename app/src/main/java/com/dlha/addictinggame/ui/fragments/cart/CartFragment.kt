package com.dlha.addictinggame.ui.fragments.cart

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlha.addictinggame.adapter.CartAdapter
import com.dlha.addictinggame.adapter.CategoryAdapter
import com.dlha.addictinggame.databinding.FragmentCartBinding
import com.dlha.addictinggame.model.GameItem
import com.dlha.addictinggame.utils.NetworkResult
import com.dlha.addictinggame.viewmodels.CartViewModel
import com.dlha.addictinggame.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class CartFragment : Fragment() {
    lateinit var binding : FragmentCartBinding


    private lateinit var cartViewModel: CartViewModel
    private lateinit var profileViewModel: ProfileViewModel

    private val mAdapter by lazy { CartAdapter(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        changeStatusBar()
        binding = FragmentCartBinding.inflate(layoutInflater)
        setupToolbar()

        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        setupRecyclerView()
        cartViewModel.userToken.observe(viewLifecycleOwner) {
            readAPI(it)
        }

        return binding.root
    }
    private fun changeStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = requireActivity().window
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
    private fun setupToolbar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.cartToolbar)
        (activity as AppCompatActivity?)!!.supportActionBar?.hide()
    }
    private fun readAPI(token : String) {
        lifecycleScope.launch {
            cartViewModel.getListGameInCart(token)
            cartViewModel.listGameInCartResponse.observe(viewLifecycleOwner) { response ->
                when(response) {
                    is NetworkResult.Loading -> {
                        showShimmerEffect()
                    }
                    is NetworkResult.Error -> {
                        hideShimmerEffect()
                        Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Success -> {
                        hideShimmerEffect()
                        response.data?.let { mAdapter.setData(it) }
                        response.data?.let { totalCoin(it) }
                    }
                }
            }
            profileViewModel.getUserInfo(token)
            profileViewModel.userInfoResponse.observe(viewLifecycleOwner) { response ->
                when(response) {
                    is NetworkResult.Loading -> {
                        showShimmerEffect()
                    }
                    is NetworkResult.Error -> {
                        hideShimmerEffect()
                        Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Success -> {
                        hideShimmerEffect()
                        binding.myCoinNumberTextView.text = response.data?.coinhave.toString()
                        binding.coinReturnNumberTextView.text = (binding.myCoinNumberTextView.text.toString().toInt() - binding.priceTextView.text.toString().toInt()).toString()
                    }
                }
            }
        }
    }
    private fun totalCoin(list : List<GameItem>) {
        var totalCoin = 0
        for(i in 0 until list?.count()!!) {
            if(list.get(i).salePercent.toInt() > 0) {
                totalCoin += ((list.get(i).coin.toFloat()*(100-list.get(i).salePercent.toFloat()))/100).roundToInt()
            } else {
                totalCoin += list.get(i).coin.toInt()
            }
        }
        binding.priceTextView.text = totalCoin.toString()
    }
    private fun setupRecyclerView() {
        binding.cartRecyclerView.adapter = mAdapter
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }
    private fun showShimmerEffect() {
        binding.cartRecyclerView.showShimmer()
    }
    private fun hideShimmerEffect() {
        binding.cartRecyclerView.hideShimmer()
    }
}