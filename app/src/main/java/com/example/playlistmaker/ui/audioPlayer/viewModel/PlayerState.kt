package com.example.playlistmaker.ui.audioPlayer.viewModel

sealed class PlayerState(val isPlayButtonEnabled: Boolean, val isPlaying: Boolean, val progress: String) {

    class Default(progress: String) : PlayerState(false, false, progress)

    class Prepared(progress: String) : PlayerState(true, false, progress)

    class Playing(progress: String) : PlayerState(true, true, progress)

    class Paused(progress: String) : PlayerState(true, false, progress)
}