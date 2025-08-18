package com.example.playlistmaker.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.api.SettingsInteractor

class App: Application() {

    private lateinit var getSettingsInteractor: SettingsInteractor

    override fun onCreate() {
        super.onCreate()
        Creator.context = this
        getSettingsInteractor = Creator.provideSettingsInteractor()
        val themeSettings = getSettingsInteractor.getThemeSettings()
        setThemeSettings(themeSettings.darkTheme)
    }

    private fun setThemeSettings(darkThemeEnabled: Boolean){
        if (darkThemeEnabled){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}