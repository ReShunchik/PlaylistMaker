package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.data.dto.SavedTrackDto
import com.example.playlistmaker.data.search.network.ITunesApi
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.storage.PrefsStorageClient
import com.example.playlistmaker.data.search.storage.StorageClient
import com.example.playlistmaker.data.settings.dto.ThemeSettingsDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val APP_PREFERENCES = "app_prefs"
private const val HISTORY_KEY = "history"
private const val THEME_SETTINGS_KEY = "theme_settings"

val dataModule = module {

    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<StorageClient<ThemeSettingsDto>>(named("SettingsStorageClient")) {
        PrefsStorageClient(
            prefs = get(),
            dataKey = THEME_SETTINGS_KEY,
            type = object : TypeToken<ThemeSettingsDto>() {}.type
        )
    }

    single<StorageClient<ArrayList<SavedTrackDto>>>(named("HistoryStorageClient")) {
        PrefsStorageClient(
            prefs = get(),
            dataKey = HISTORY_KEY,
            type = object : TypeToken<ArrayList<SavedTrackDto>>() {}.type
        )
    }


}

