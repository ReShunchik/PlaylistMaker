package com.example.playlistmaker.di

import com.example.playlistmaker.domain.search.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.SearchInteractor
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.example.playlistmaker.domain.settings.api.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.api.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module
import java.util.concurrent.Executors


val interactorModule = module {

    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    single {
        Executors.newCachedThreadPool()
    }

    factory<SearchInteractor> {
        SearchInteractorImpl(get(), get())
    }

    factory<SharingInteractor>{
        SharingInteractorImpl(get())
    }
}
