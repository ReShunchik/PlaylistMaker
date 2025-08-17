package com.example.playlistmaker.data.settings.mapper

import com.example.playlistmaker.data.settings.dto.ThemeSettingsDto
import com.example.playlistmaker.domain.settings.models.ThemeSettings

object ThemeSettingsMapper {

    fun mapToDto(themeSettings: ThemeSettings): ThemeSettingsDto{
        return ThemeSettingsDto(themeSettings.darkTheme)
    }

    fun map(themeSettings: ThemeSettingsDto): ThemeSettings{
        return ThemeSettings(themeSettings.darkTheme)
    }

}