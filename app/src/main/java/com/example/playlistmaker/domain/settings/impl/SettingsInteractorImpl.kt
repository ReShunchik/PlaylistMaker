package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.data.settings.mapper.ThemeSettingsMapper
import com.example.playlistmaker.domain.settings.api.SettingsInteractor
import com.example.playlistmaker.domain.settings.api.SettingsRepository
import com.example.playlistmaker.domain.settings.models.ThemeSettings

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository
): SettingsInteractor {

    override fun getThemeSettings(): ThemeSettings {
        val themeSettings = settingsRepository.getThemeSettings()
        return themeSettings
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsRepository.updateThemeSetting(ThemeSettingsMapper.mapToDto(settings))
    }

}