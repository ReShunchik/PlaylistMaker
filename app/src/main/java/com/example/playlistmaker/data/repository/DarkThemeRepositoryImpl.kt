package com.example.playlistmaker.data.repository

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.DarkThemeRepository

class DarkThemeRepositoryImpl(
    private val sharedPreferences: SharedPreferences
): DarkThemeRepository{

    override fun saveDarkThemeEnabled(enabled: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(DARKTHEME, enabled)
            .apply()
    }

    override fun isDarkThemeEnabled(): Boolean {
        return sharedPreferences.getBoolean(DARKTHEME, false)
    }

    companion object{
        const val DARKTHEME = "dark_theme"
    }

}