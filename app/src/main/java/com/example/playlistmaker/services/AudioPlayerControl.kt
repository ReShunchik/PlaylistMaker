package com.example.playlistmaker.services

import com.example.playlistmaker.ui.audioPlayer.viewModel.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun getCurrentPlayerState(): StateFlow<PlayerState>
    fun startPlayer()
    fun pausePlayer()
    fun hideNotification()
    fun showNotification()
}