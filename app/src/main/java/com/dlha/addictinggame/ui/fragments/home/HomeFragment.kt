package com.dlha.addictinggame.ui.fragments.home

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dlha.addictinggame.R
import com.dlha.addictinggame.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        changeStatusBar()
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        setupToolbar()

        binding.newViewAllTextView.setOnClickListener {
           findNavController().navigate(R.id.action_homeFragment_to_newGameActivity)
        }

        return binding.root

    }
    private fun changeStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = requireActivity().window
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
    private fun setupToolbar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.homeToolbar)
        (activity as AppCompatActivity?)!!.supportActionBar?.hide()
    }

}