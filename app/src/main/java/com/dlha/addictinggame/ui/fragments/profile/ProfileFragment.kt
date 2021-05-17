package com.dlha.addictinggame.ui.fragments.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.dlha.addictinggame.R

import com.dlha.addictinggame.databinding.FragmentProfileBinding
import com.dlha.addictinggame.ui.activities.LoginActivity


class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        changeStatusBar()
        setupToolbar()

        binding.loginLayout.setOnClickListener {
            startActivity(Intent(activity,LoginActivity::class.java))
            activity?.overridePendingTransition(R.anim.fly,R.anim.stay)
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
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.profileToolbar)
        (activity as AppCompatActivity?)!!.supportActionBar?.hide()
    }


}