package com.example.playlistmaker.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.App
import com.example.playlistmaker.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val buttonBack = findViewById<ImageView>(R.id.button_back)
        val buttonShare = findViewById<TextView>(R.id.share_app)
        val buttonSupport = findViewById<TextView>(R.id.email_support)
        val buttonAgreement = findViewById<TextView>(R.id.user_agreement)
        val themesSwitcher = findViewById<SwitchCompat>(R.id.themes_swither)

        val themesApp = (applicationContext as App)
        themesSwitcher.isChecked = themesApp.darkTheme

        buttonBack.setOnClickListener{
            finish()
        }

        themesSwitcher.setOnCheckedChangeListener { switcher, isChecked ->
            themesApp.switchTheme(isChecked)
        }

        buttonShare.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
            }
            startActivity(shareIntent)
        }

        buttonSupport.setOnClickListener{
            val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("v.gorshenin_2004@mail.ru"))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_message))
            }
            startActivity(supportIntent)
        }

        buttonAgreement.setOnClickListener{
            val agreement = Uri.parse(getString(R.string.agreement_link))
            val agreementIntent = Intent(Intent.ACTION_VIEW, agreement)
            startActivity(agreementIntent)
        }
    }
}