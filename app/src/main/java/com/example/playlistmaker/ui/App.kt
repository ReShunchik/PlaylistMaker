package com.example.playlistmaker.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.api.SettingsInteractor

class App: Application() {

    private lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate() {
        super.onCreate()
        Creator.context = this
        settingsInteractor = Creator.provideSettingsInteractor()
        settingsInteractor.applySavedTheme()
    }
}