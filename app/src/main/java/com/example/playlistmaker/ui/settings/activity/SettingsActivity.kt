package com.example.playlistmaker.ui.settings.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.viewModel.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getFactory(this)
        ).get(SettingsViewModel::class.java)

        viewModel.observeDarkMode().observe(this){
            binding.themesSwitcher.isChecked = it
        }

        binding.buttonBack.setOnClickListener{
            finish()
        }

        binding.themesSwitcher.setOnCheckedChangeListener { switcher, isChecked ->
            viewModel.switchTheme(isChecked)
        }

        binding.shareApp.setOnClickListener{
            viewModel.shareApp()
        }

        binding.emailSupport.setOnClickListener{
            viewModel.openSupport()
        }

        binding.terms.setOnClickListener{
            viewModel.openTerms()
        }
    }
}