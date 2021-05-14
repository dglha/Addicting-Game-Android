package com.dlha.addictinggame.ui.fragments.categories

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dlha.addictinggame.R
import com.dlha.addictinggame.adapter.CategoryAdapter
import com.dlha.addictinggame.databinding.FragmentCategoriesBinding


class CategoriesFragment : Fragment() {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)
        setupActionbar()

        val categoryRecyclerView = binding.categoriesRecyclerView
        categoryRecyclerView.adapter = CategoryAdapter()
        Log.d("test","TESING")
        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.optionsmenu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    private fun setupActionbar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
    }


}
