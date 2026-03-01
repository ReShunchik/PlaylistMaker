package com.example.playlistmaker.ui.audioPlayer.viewModel

sealed class PlayerState(val buttonState: Boolean, val progress: String) {

    class Default() : PlayerState(false,"00:00")

    class Prepared() : PlayerState(true, "00:00")

    class Playing(progress: String) : PlayerState(true, progress)

    class Paused(progress: String) : PlayerState(true, progress)
}