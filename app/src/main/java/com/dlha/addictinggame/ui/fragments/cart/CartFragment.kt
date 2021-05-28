package com.dlha.addictinggame.ui.fragments.cart

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dlha.addictinggame.adapter.CartAdapter
import com.dlha.addictinggame.databinding.FragmentCartBinding
import com.dlha.addictinggame.model.GameItem
import com.dlha.addictinggame.utils.NetworkResult
import com.dlha.addictinggame.utils.SwipeToDelete
import com.dlha.addictinggame.viewmodels.CartViewModel
import com.dlha.addictinggame.viewmodels.ProfileViewModel
import com.todkars.shimmer.ShimmerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class CartFragment : Fragment() {
    lateinit var binding : FragmentCartBinding


    private lateinit var cartViewModel: CartViewModel
    private lateinit var profileViewModel: ProfileViewModel

    private lateinit var token: String

    private var listID : MutableList<Int> = mutableListOf()
    private lateinit var listIDString : String

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
        swipeToDelete(binding.cartRecyclerView)


        cartViewModel.userToken.observe(viewLifecycleOwner) {
            readAPI(it)
            getUserInfo(it)
            token = it

        }

        binding.checkoutButton.setOnClickListener {
            checkout(token,listIDString)
            binding.cartProgressBar.visibility = View.VISIBLE
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
                        mAdapter.setData(emptyList())
                        hideShimmerEffect()
                        Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Success -> {
                        hideShimmerEffect()
//                        getUserInfo(token)
                        response.data?.let { mAdapter.setData(it) }
                        response.data?.let { totalCoin(it) }
                    }
                }
            }

        }
    }
    private fun getUserInfo(token: String){
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
    private fun checkout(token : String,listID : String) {
        lifecycleScope.launch {
            cartViewModel.checkout(token,listID)
            cartViewModel.messageResponse.observe(viewLifecycleOwner) {response ->
                when(response) {
                    is NetworkResult.Loading -> {
                        binding.cartProgressBar.visibility = View.VISIBLE
                    }
                    is NetworkResult.Error -> {
                        binding.cartProgressBar.visibility = View.INVISIBLE
                    }
                    is NetworkResult.Success -> {
                        binding.cartProgressBar.visibility = View.INVISIBLE
                        readAPI(token)
                        Toast.makeText(requireContext(),"Payment success",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun totalCoin(list : List<GameItem>) {
        var totalCoin = 0
        listID = mutableListOf()
        for(i in 0 until list?.count()!!) {
            totalCoin += if(list.get(i).salePercent.toInt() > 0) {
                ((list.get(i).coin.toFloat()*(100-list.get(i).salePercent.toFloat()))/100).roundToInt()
            } else {
                list.get(i).coin.toInt()
            }
            listID.add(list[i].id)
        }
        binding.priceTextView.text = totalCoin.toString()
        listIDString = listID.toString().substring(1,listID.toString().length-1)
        Log.d("LISTIDD",listIDString)
    }
    private fun setupRecyclerView() {
        binding.cartRecyclerView.adapter = mAdapter
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        showShimmerEffect()
    }

    private fun swipeToDelete(cartRecyclerView: ShimmerRecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = mAdapter.gamesInCart[viewHolder.bindingAdapterPosition]
                // Delete item
                lifecycleScope.launch {
                    cartViewModel.removeGameFromCart(deletedItem.id)
                    cartViewModel.messageResponse.observe(
                        viewLifecycleOwner
                    ) { response ->
                        when (response) {
                            is NetworkResult.Loading -> {
                            }
                            is NetworkResult.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    response.message,
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                            is NetworkResult.Success -> {
                                Toast.makeText(requireContext(), "Removed ${deletedItem.name}!", Toast.LENGTH_SHORT).show()
                                //                                    mAdapter.setData(emptyList())
                                cartViewModel.getListGameInCart(token)
                            }
                        }
                    }
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(cartRecyclerView)
    }

    private fun showShimmerEffect() {
        binding.cartRecyclerView.showShimmer()
    }
    private fun hideShimmerEffect() {
        binding.cartRecyclerView.hideShimmer()
    }
}