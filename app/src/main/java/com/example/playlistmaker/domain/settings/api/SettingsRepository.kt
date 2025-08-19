package com.example.playlistmaker.domain.settings.api

import com.example.playlistmaker.data.settings.dto.ThemeSettingsDto
import com.example.playlistmaker.domain.settings.models.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettingsDto)
    fun applySavedTheme()
}