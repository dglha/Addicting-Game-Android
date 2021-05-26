package com.dlha.addictinggame.ui.fragments.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import coil.load
import com.dlha.addictinggame.R
import com.dlha.addictinggame.databinding.FragmentProfileBinding
import com.dlha.addictinggame.model.User
import com.dlha.addictinggame.ui.activities.FavoritesActivity
import com.dlha.addictinggame.ui.activities.GameHavingActivity
import com.dlha.addictinggame.ui.activities.LoginActivity
import com.dlha.addictinggame.utils.NetworkResult
import com.dlha.addictinggame.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by activityViewModels()

    private var userSave: User? = null;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        changeStatusBar()
        setupToolbar()

        lifecycleScope.launch {
            readUserInfo()
        }

        binding.logoutLayout.visibility = View.VISIBLE

        binding.loginLayout.setOnClickListener {
//            findNavController().navigate(R.id.action_profileFragment_to_loginActivity)
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        binding.logoutLayout.setOnClickListener {
            lifecycleScope.launch {
                profileViewModel.clearAuthKey()
                resetContentView()
            }
        }

        return binding.root
    }

    private fun readUserInfo() {
        profileViewModel.userToken.observe(viewLifecycleOwner) { token ->
            if (!token.equals("null")) {
                Log.d("Obs", "readUserInfo: token: $token")
                profileViewModel.getUserInfo(token)
                profileViewModel.userInfoResponse.observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is NetworkResult.Error -> {
                            resetContentView()
                        }
                        is NetworkResult.Success -> {
                            setupContentView(response.data!!)
                        }
                        is NetworkResult.Loading -> {
                            Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            } else {
                Log.d("Obs", "readUserInfo: token: $token")
                resetContentView()
            }
        }
    }


    private fun setupContentView(user: User) {
        userSave = user
        val name = user.firstname + " " + user.lastname

        binding.usernameTextView.text = name
        binding.avatar.load(user.avatar)
        binding.coinTextView.text = user.coinhave.toString()

        binding.loginLayout.visibility = View.GONE
        binding.favoritesGameLayout.visibility = View.VISIBLE
        binding.gamehavingLayout.visibility = View.VISIBLE
        binding.logoutLayout.visibility = View.VISIBLE

        binding.totalNumberFavorites.text = "Already have "+user.totalFavorites.toString()+" games"

        binding.gamehavingLayout.setOnClickListener {
            startActivity(Intent(requireContext(), GameHavingActivity::class.java))
        }

        binding.favoritesGameLayout.setOnClickListener {
            startActivity(Intent(requireContext(),FavoritesActivity::class.java))
        }

    }

    private fun resetContentView() {
        Log.d("Profile", "resetContentView: called")
        binding.usernameTextView.text = "USER"
        binding.avatar.load(R.drawable._c3ad823775971_5632898dbfbfa)

        binding.loginLayout.visibility = View.VISIBLE
        binding.logoutLayout.visibility = View.GONE
        binding.favoritesGameLayout.visibility = View.GONE
        binding.gamehavingLayout.visibility = View.GONE





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
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.myprofileToolbar)
        (activity as AppCompatActivity?)!!.supportActionBar?.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}