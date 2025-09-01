package com.example.playlistmaker.di

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.ui.audioPlayer.viewModel.AudioPlayerViewModel
import com.example.playlistmaker.ui.search.viewModel.SearchViewModel
import com.example.playlistmaker.ui.settings.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.text.SimpleDateFormat
import java.util.Locale


val viewModelModule = module {

    viewModel<SettingsViewModel> {
        SettingsViewModel(
            sharingInteractor = get(),
            settingsInteractor = get()
        )
    }

    single {
        Handler(Looper.getMainLooper())
    }

    viewModel<SearchViewModel> {
        SearchViewModel(get(), get(), get())
    }

    single {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }

    factory {
        MediaPlayer()
    }

    viewModel<AudioPlayerViewModel> {
        (url: String) ->
            AudioPlayerViewModel(get(), url, get(), get())
    }
}
