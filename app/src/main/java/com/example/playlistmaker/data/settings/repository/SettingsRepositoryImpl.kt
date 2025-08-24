package com.example.playlistmaker.data.settings.repository

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.search.storage.StorageClient
import com.example.playlistmaker.data.settings.dto.ThemeSettingsDto
import com.example.playlistmaker.data.settings.mapper.ThemeSettingsMapper
import com.example.playlistmaker.domain.settings.api.SettingsRepository
import com.example.playlistmaker.domain.settings.models.ThemeSettings

class SettingsRepositoryImpl(
    private val storageClient: StorageClient<ThemeSettingsDto>
): SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        val themeSettings = storageClient.getData() ?: ThemeSettingsDto(false)
        return ThemeSettingsMapper.map(themeSettings)
    }

    override fun updateThemeSetting(settings: ThemeSettingsDto) {
        storageClient.storeData(settings)

        if (settings.darkTheme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun applySavedTheme() {
        if (getThemeSettings().darkTheme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}