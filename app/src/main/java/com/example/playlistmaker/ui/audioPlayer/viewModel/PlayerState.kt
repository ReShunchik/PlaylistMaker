package com.example.playlistmaker.ui.audioPlayer.viewModel

sealed interface PlayerState {
    object Prepared: PlayerState
    data class Playing(
        val currentTime: String
    ) : PlayerState
    object Paused: PlayerState
    object Default: PlayerState
    object Finished: PlayerState
}