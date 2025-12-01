package com.example.playlistmaker.di

import com.example.playlistmaker.data.favorite.repository.FavoriteRepositoryImpl
import com.example.playlistmaker.data.playlist.repository.ImageRepositoryImpl
import com.example.playlistmaker.domain.favorite.api.FavoriteInteractor
import com.example.playlistmaker.domain.favorite.impl.FavoriteInteractorImpl
import com.example.playlistmaker.domain.playlist.api.ImageInteractor
import com.example.playlistmaker.domain.playlist.api.PlaylistInteractor
import com.example.playlistmaker.domain.playlist.impl.ImageInteractorImpl
import com.example.playlistmaker.domain.playlist.impl.PlaylistInteractorImpl
import com.example.playlistmaker.domain.search.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.SearchInteractor
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.example.playlistmaker.domain.settings.api.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.api.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module


val interactorModule = module {

    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    factory<SearchInteractor> {
        SearchInteractorImpl(get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    factory<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }

    factory<PlaylistInteractor> {
        PlaylistInteractorImpl(get(), get())
    }

    factory<ImageInteractor> {
        ImageInteractorImpl(get())
    }
}
