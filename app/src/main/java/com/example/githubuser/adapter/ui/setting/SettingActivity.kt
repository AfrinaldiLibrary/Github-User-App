package com.example.githubuser.adapter.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.adapter.ui.home.MainViewModel
import com.example.githubuser.adapter.ui.home.MainViewModelFactory
import com.example.githubuser.adapter.ui.home.dataStore
import com.example.githubuser.databinding.ActivitySettingBinding
import com.example.githubuser.network.database.SettingPreferences

class SettingActivity : AppCompatActivity() {

    private var _binding: ActivitySettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Setting"

        getThemeSetting()
    }

    private fun getThemeSetting() {
        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(pref))[MainViewModel::class.java]

        mainViewModel.getThemeSettings().observe(this@SettingActivity) { selectedTheme: Int ->
            when (selectedTheme) {
                0 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.lightTheme.isChecked = true
                }
                1 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.darkTheme.isChecked = true
                }
            }
        }

        binding.themeContainer.setOnCheckedChangeListener { _, id ->
            when (id) {
                binding.lightTheme.id -> mainViewModel.saveThemeSetting(0)
                binding.darkTheme.id -> mainViewModel.saveThemeSetting(1)
            }
        }
    }
}