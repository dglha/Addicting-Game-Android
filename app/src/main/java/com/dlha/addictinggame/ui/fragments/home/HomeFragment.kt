package com.dlha.addictinggame.ui.fragments.home

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlha.addictinggame.R
import com.dlha.addictinggame.adapter.NewGameModuleAdapter
import com.dlha.addictinggame.adapter.SaleGameModuleAdapter
import com.dlha.addictinggame.databinding.FragmentHomeBinding
import com.dlha.addictinggame.utils.NetworkResult
import com.dlha.addictinggame.viewmodels.MainViewModel
import com.todkars.shimmer.ShimmerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel

    private val mNewGameModuleAdapter by lazy { NewGameModuleAdapter(requireContext()) }
    private val mSaleGameModuleAdapter by lazy { SaleGameModuleAdapter(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        changeStatusBar()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupToolbar()

        setupRecycleView()
        mainViewModel.userToken.observe(viewLifecycleOwner) {
            if(it=="null") {
                readApi("")
            } else {
                readApi(it)
            }
        }


        binding.newViewAllTextView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_newGameActivity)
        }
        binding.saleViewAllTextView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_saleActivity)
        }

        return binding.root

    }

    private fun setupRecycleView() {
        // adapter new
        binding.newGameRecyclerView.adapter = mNewGameModuleAdapter
        binding.newGameRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        showShimmerEffect(binding.newGameRecyclerView)
        // adapter sale
        binding.saleGameRecyclerView.adapter = mSaleGameModuleAdapter
        binding.saleGameRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        showShimmerEffect(binding.saleGameRecyclerView)
    }

    private fun showShimmerEffect(recyclerView: ShimmerRecyclerView) {
        recyclerView.showShimmer()
    }

    private fun hideShimmerEffect(recyclerView: ShimmerRecyclerView) {
        recyclerView.hideShimmer()
    }

    private fun readApi(token : String) {
        // read New Game
        lifecycleScope.launch {
            mainViewModel.getNewGames(token)
            mainViewModel.newGamesResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        Log.d("ListNewGame", "readApi: success")
                        hideShimmerEffect(binding.newGameRecyclerView)
                        response.data?.let {
                            mNewGameModuleAdapter.setData(it)
                        }
                    }
                    is NetworkResult.Error -> {
                        hideShimmerEffect(binding.newGameRecyclerView)
                        Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Loading -> {
                        showShimmerEffect(binding.newGameRecyclerView)
                    }
                }
            }
            // read Sale Game
            mainViewModel.getSaleGames(token)
            mainViewModel.saleGameResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        Log.d("ListSaleGame", "readApi: success")
                        hideShimmerEffect(binding.saleGameRecyclerView)
                        response.data?.let {
                            mSaleGameModuleAdapter.setData(it)
                        }
                    }
                    is NetworkResult.Error -> {
                        hideShimmerEffect(binding.saleGameRecyclerView)
                        Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Loading -> {
                        showShimmerEffect(binding.saleGameRecyclerView)
                    }
                }
            }
        }
    }

    private fun changeStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = requireActivity().window
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );
        }
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.homeToolbar)
        (activity as AppCompatActivity?)!!.supportActionBar?.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}