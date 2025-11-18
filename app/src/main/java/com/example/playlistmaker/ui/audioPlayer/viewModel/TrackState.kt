package com.example.playlistmaker.ui.audioPlayer.viewModel

sealed interface TrackState {

    object IsFavorite: TrackState

    object NotFavorite: TrackState

}