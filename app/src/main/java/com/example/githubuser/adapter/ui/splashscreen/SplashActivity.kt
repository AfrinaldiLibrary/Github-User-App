package com.example.githubuser.adapter.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.R
import com.example.githubuser.adapter.ui.home.MainActivity
import com.example.githubuser.adapter.ui.home.MainViewModel
import com.example.githubuser.adapter.ui.home.MainViewModelFactory
import com.example.githubuser.adapter.ui.home.dataStore
import com.example.githubuser.network.database.SettingPreferences

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        getThemeSetting()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, TIMEOUT)
    }

    private fun getThemeSetting() {
        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(pref))[MainViewModel::class.java]

        mainViewModel.getThemeSettings().observe(this@SplashActivity) { selectedTheme: Int ->
            when (selectedTheme) {
                0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    companion object {
        const val TIMEOUT: Long = 3000
    }
}