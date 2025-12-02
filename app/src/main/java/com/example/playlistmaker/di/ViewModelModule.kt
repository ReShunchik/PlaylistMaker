package com.example.playlistmaker.di

import android.media.MediaPlayer
import com.example.playlistmaker.ui.audioPlayer.viewModel.AudioPlayerViewModel
import com.example.playlistmaker.ui.createPlaylist.viewModel.CreatePlaylistViewModel
import com.example.playlistmaker.ui.mediaLibrary.viewModel.FavoriteViewModel
import com.example.playlistmaker.ui.mediaLibrary.viewModel.PlaylistViewModel
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

    viewModel<SearchViewModel> {
        SearchViewModel(get(), get())
    }

    single {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }

    factory {
        MediaPlayer()
    }

    viewModel<AudioPlayerViewModel> {
        (url: String) ->
            AudioPlayerViewModel(get(), url, get(), get(), get(), get())
    }

    viewModel<FavoriteViewModel> {
        FavoriteViewModel(get(), get())
    }

    viewModel<CreatePlaylistViewModel>{
        CreatePlaylistViewModel(get())
    }

    viewModel<PlaylistViewModel>{
        PlaylistViewModel(get())
    }
}
