package com.example.playlistmaker.ui.mediaLibrary.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.example.playlistmaker.ui.mediaLibrary.adapters.MediaLibraryViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener{
            finish()
        }

        binding.viewPager.adapter = MediaLibraryViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favourite)
                1 -> tab.text = getString(R.string.playlist)
            }
        }
        tabMediator.attach()
    }


    fun String.capitalizeFirstLowerRest(): String {
        return this.lowercase().replaceFirstChar { it.uppercase() }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}