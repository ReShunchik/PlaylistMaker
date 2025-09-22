package com.example.playlistmaker.di

import com.example.playlistmaker.data.search.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.repository.SearchRepositoryImpl
import com.example.playlistmaker.data.settings.repository.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.SharingRepositoryImpl
import com.example.playlistmaker.domain.search.api.SearchHistoryRepository
import com.example.playlistmaker.domain.search.api.SearchRepository
import com.example.playlistmaker.domain.settings.api.SettingsRepository
import com.example.playlistmaker.domain.sharing.api.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module


val repositoryModule = module {

    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(
            storageClient = get(named("HistoryStorageClient")))
    }

    factory<SearchRepository> {
        SearchRepositoryImpl(get())
    }

    factory<SettingsRepository> {
        SettingsRepositoryImpl(
            storageClient = get(named("SettingsStorageClient")))
    }

    factory<SharingRepository> {
        SharingRepositoryImpl(androidContext())
    }

}

