package com.example.playlistmaker.ui.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.ui.settings.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.databinding.FragmentSettingsBinding

class SettingsFragment: Fragment() {

    private lateinit var _binding: FragmentSettingsBinding
    private val binding get() = _binding!!
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                          savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeDarkMode().observe(viewLifecycleOwner){
            binding.themesSwitcher.isChecked = it
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