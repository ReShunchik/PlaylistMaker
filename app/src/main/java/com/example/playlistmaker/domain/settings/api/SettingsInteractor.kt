package com.example.playlistmaker.domain.settings.api

import com.example.playlistmaker.domain.settings.models.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}