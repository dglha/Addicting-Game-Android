package com.dlha.addictinggame.ui.fragments.categories

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlha.addictinggame.R
import com.dlha.addictinggame.adapter.CategoryAdapter
import com.dlha.addictinggame.databinding.FragmentCategoriesBinding
import com.dlha.addictinggame.ui.activities.SearchActivity
import com.dlha.addictinggame.utils.NetworkResult
import com.dlha.addictinggame.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoriesFragment : Fragment(), SearchView.OnQueryTextListener {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel

    private val mAdapter by lazy { CategoryAdapter(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setHasOptionsMenu(true)
        setupToolbar()

        setupRecyclerView()
        readApi()

        return binding.root
    }
    private fun readApi() {
        lifecycleScope.launch {
            mainViewModel.getCategory()
            mainViewModel.categoryResponse.observe(viewLifecycleOwner) { response ->
              when(response) {
                  is NetworkResult.Loading -> {
                      showShimmerEffect()
                  }
                  is NetworkResult.Error -> {
                      hideShimmerEffect()
                      Toast.makeText(requireContext(),response.message.toString(),Toast.LENGTH_SHORT).show()
                  }
                  is NetworkResult.Success -> {
                      hideShimmerEffect()
                      response?.data?.let { mAdapter.setData(it) }
                  }
              }

            }
        }
    }
    private fun setupRecyclerView() {
        binding.categoriesRecyclerView.adapter = mAdapter
        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }
    private fun setupToolbar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.categoriesToolbar)
        (activity as AppCompatActivity?)!!.supportActionBar?.show()
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayShowTitleEnabled(false)
        changeStatusBar()
    }
    private fun changeStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = requireActivity().window
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
    private fun showShimmerEffect() {
        binding.categoriesRecyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.categoriesRecyclerView.hideShimmer()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.optionsmenu,menu)

        val searchItem = menu.findItem(R.id.menu_search)

        val searchView : SearchView? = searchItem.actionView as SearchView?

        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        startActivity(Intent(activity,SearchActivity::class.java).putExtra("search",query))
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

}
