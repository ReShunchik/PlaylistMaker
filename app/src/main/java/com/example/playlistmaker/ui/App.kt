package com.example.playlistmaker.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.DarkThemeInteractor

class App: Application() {

    var darkTheme = false
    private lateinit var getDarkThemeInteractor: DarkThemeInteractor

    override fun onCreate() {
        super.onCreate()
        getDarkThemeInteractor = Creator.provideDarkThemeInteractor(this)
        darkTheme = getDarkThemeInteractor.isDarkThemeEnabled()
        applySavedTheme()
    }

    private fun applySavedTheme(){
        if (darkTheme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean){
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        getDarkThemeInteractor.saveDarkThemeEnabled(darkThemeEnabled)
    }
}