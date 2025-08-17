package com.example.playlistmaker.ui.audioPlayer.viewModel

sealed interface PlayerState {
    object Prepared: PlayerState
    object Playing: PlayerState
    object Paused: PlayerState
    object Default: PlayerState
}