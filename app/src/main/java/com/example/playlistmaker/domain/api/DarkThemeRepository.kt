package com.example.playlistmaker.domain.api

interface DarkThemeRepository {
    fun saveDarkThemeEnabled(enabled: Boolean)
    fun isDarkThemeEnabled(): Boolean
}