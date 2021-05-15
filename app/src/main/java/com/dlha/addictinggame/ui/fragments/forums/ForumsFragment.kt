package com.dlha.addictinggame.ui.fragments.forums

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.dlha.addictinggame.databinding.FragmentForumsBinding


class ForumsFragment : Fragment() {
    private lateinit var binding : FragmentForumsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        changeStatusBar()
        binding = FragmentForumsBinding.inflate(layoutInflater)
        setupToolbar()

        return binding.root
    }
    private fun changeStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = requireActivity().window
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
    private fun setupToolbar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.forumsToolbar)
        (activity as AppCompatActivity?)!!.supportActionBar?.hide()
    }
}