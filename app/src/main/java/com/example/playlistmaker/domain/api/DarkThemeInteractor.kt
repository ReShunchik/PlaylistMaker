package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.api.consumer.Consumer

interface DarkThemeInteractor {
    fun saveDarkThemeEnabled(enabled: Boolean)
    fun isDarkThemeEnabled(): Boolean
}