package com.dlha.addictinggame.adapter

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.dlha.addictinggame.databinding.ActivityPlayGameBinding

class PlayGameActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPlayGameBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayGameBinding.inflate(layoutInflater)
        setContentView(binding.root)


        changeStatusBar()

        webviewSetup(intent.getStringExtra("webgame"))
        intent.getStringExtra("webgame")?.let { Log.d("LINK", it) }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun webviewSetup(linkGame : String?) {
        binding.playgameWebView.webViewClient = WebViewClient()

        binding.playgameWebView.apply {
            if (linkGame != null) {
                loadUrl("https://i.simmer.io/@Nannings/synth-drift")
            }
            settings.javaScriptEnabled = true
            settings.safeBrowsingEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            setInitialScale(1)
        }
    }
    private fun changeStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }
}