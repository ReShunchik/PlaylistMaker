package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
            val buttonBack = findViewById<ImageView>(R.id.button_back)
            buttonBack.setOnClickListener{
                val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                startActivity(intent)
            }
    }
}