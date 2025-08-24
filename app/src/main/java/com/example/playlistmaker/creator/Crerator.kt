package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.dto.SavedTrackDto
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.repository.SearchRepositoryImpl
import com.example.playlistmaker.data.search.storage.PrefsStorageClient
import com.example.playlistmaker.data.settings.dto.ThemeSettingsDto
import com.example.playlistmaker.data.settings.repository.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.SharingRepositoryImpl
import com.example.playlistmaker.domain.search.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.SearchHistoryRepository
import com.example.playlistmaker.domain.search.api.SearchInteractor
import com.example.playlistmaker.domain.search.api.SearchRepository
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.example.playlistmaker.domain.settings.api.SettingsInteractor
import com.example.playlistmaker.domain.settings.api.SettingsRepository
import com.example.playlistmaker.domain.settings.api.SharingInteractor
import com.example.playlistmaker.domain.settings.api.SharingRepository
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.settings.impl.SharingInteractorImpl
import com.google.gson.reflect.TypeToken

object Creator {

    lateinit var context: Context

    private const val APP_PREFERENCES = "app_prefs"
    private const val HISTORY_KEY = "history"
    private const val THEME_SETTINGS_KEY = "theme_settings"

    private fun getTracksRepository(): SearchRepository {
        return SearchRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(getTracksRepository())
    }

    private fun getSharedPreferences(): SharedPreferences{
        return context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    private fun getSharingRepository(context: Context): SharingRepository{
        return SharingRepositoryImpl(context)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor{
        return SharingInteractorImpl(getSharingRepository(context))
    }

    private fun getSettingsRepository(): SettingsRepository{
        return SettingsRepositoryImpl(
            PrefsStorageClient(
                getSharedPreferences(),
                THEME_SETTINGS_KEY,
                object : TypeToken<ThemeSettingsDto>() {}.type)
        )
    }

    fun provideSettingsInteractor(): SettingsInteractor{
        return SettingsInteractorImpl(getSettingsRepository())
    }

    private fun getSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(
            PrefsStorageClient(
            getSharedPreferences(),
            HISTORY_KEY,
            object : TypeToken<ArrayList<SavedTrackDto>>() {}.type)
        )
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository())
    }

}