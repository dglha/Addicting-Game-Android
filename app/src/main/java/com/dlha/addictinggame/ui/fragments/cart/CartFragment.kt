package com.dlha.addictinggame.ui.fragments.cart

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.dlha.addictinggame.adapter.CartAdapter
import com.dlha.addictinggame.databinding.FragmentCartBinding



class CartFragment : Fragment() {
    lateinit var binding : FragmentCartBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        changeStatusBar()
        binding = FragmentCartBinding.inflate(layoutInflater)
        setupToolbar()

        var cartRecyclerView = binding.cartRecyclerView
        cartRecyclerView.adapter = CartAdapter()

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

}