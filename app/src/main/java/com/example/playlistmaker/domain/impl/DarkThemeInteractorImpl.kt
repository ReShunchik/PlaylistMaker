package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.DarkThemeInteractor
import com.example.playlistmaker.domain.api.DarkThemeRepository

class DarkThemeInteractorImpl(
    private val darkThemeRepository: DarkThemeRepository
): DarkThemeInteractor {
    override fun saveDarkThemeEnabled(enabled: Boolean) {
        darkThemeRepository.saveDarkThemeEnabled(enabled)
    }

    override fun isDarkThemeEnabled(): Boolean {
        return darkThemeRepository.isDarkThemeEnabled()
    }
}