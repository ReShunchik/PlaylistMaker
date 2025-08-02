package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.repository.DarkThemeRepositoryImpl
import com.example.playlistmaker.data.repository.TracksHistoryRepositoryImpl
import com.example.playlistmaker.data.repository.TracksRepositoryImpl
import com.example.playlistmaker.domain.api.DarkThemeInteractor
import com.example.playlistmaker.domain.api.DarkThemeRepository
import com.example.playlistmaker.domain.api.TracksHistoryInteractor
import com.example.playlistmaker.domain.api.TracksHistoryRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.DarkThemeInteractorImpl
import com.example.playlistmaker.domain.impl.TracksHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getSharedPreferences(context: Context): SharedPreferences{
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    private fun getDarkThemeRepository(context: Context): DarkThemeRepository{
        return DarkThemeRepositoryImpl(getSharedPreferences(context))
    }

    fun provideDarkThemeInteractor(context: Context): DarkThemeInteractor{
        return DarkThemeInteractorImpl(getDarkThemeRepository(context))
    }

    private fun getTrackHistoryRepository(context: Context): TracksHistoryRepository{
        return TracksHistoryRepositoryImpl(getSharedPreferences(context))
    }

    fun provideTrackHistoryInteractor(context: Context): TracksHistoryInteractor{
         return TracksHistoryInteractorImpl(getTrackHistoryRepository(context))
     }

}