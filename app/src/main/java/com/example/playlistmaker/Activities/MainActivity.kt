package com.example.playlistmaker.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<FrameLayout>(R.id.search_button)
        val mediaLibraryButton = findViewById<FrameLayout>(R.id.media_library_button)
        val settingsButton = findViewById<FrameLayout>(R.id.settins_button)

        val myButtonClickListener = object: View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }
        }
        searchButton.setOnClickListener(myButtonClickListener)
        mediaLibraryButton.setOnClickListener{
            val intent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(intent)
        }
        settingsButton.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}