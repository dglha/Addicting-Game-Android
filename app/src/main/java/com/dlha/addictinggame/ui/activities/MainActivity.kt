package com.dlha.addictinggame.ui.activities

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dlha.addictinggame.R
import com.dlha.addictinggame.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.hide()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.homeFragment, R.id.profileFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        loadLocate()
    }

    private fun loadLocate() {
        val sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        if (language != null) {
            setLocate(language)
        }
    }

    fun setLocate(lang : String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext?.resources?.updateConfiguration(config, baseContext?.resources!!.displayMetrics)
    }

}