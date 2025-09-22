package com.example.playlistmaker.ui.settings.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.api.SettingsInteractor
import com.example.playlistmaker.domain.sharing.api.SharingInteractor
import com.example.playlistmaker.domain.settings.models.ThemeSettings

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
): ViewModel(){


    private val darkModeLiveData = MutableLiveData<Boolean>(getThemeSettings().darkTheme)
    fun observeDarkMode(): LiveData<Boolean> = darkModeLiveData

    fun shareApp(){
        sharingInteractor.shareApp()
    }

    fun openTerms(){
        sharingInteractor.openTerms()
    }

    fun openSupport(){
        sharingInteractor.openSupport()
    }

    fun switchTheme(darkTheme: Boolean){
        darkModeLiveData.postValue(darkTheme)
        settingsInteractor.updateThemeSetting(ThemeSettings(darkTheme))
    }

    private fun getThemeSettings(): ThemeSettings {
        return settingsInteractor.getThemeSettings()
    }
}