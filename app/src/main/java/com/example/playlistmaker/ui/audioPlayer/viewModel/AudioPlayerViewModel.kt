package com.example.playlistmaker.ui.audioPlayer.viewModel

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val mediaPlayer: MediaPlayer,
    private val url: String
) : ViewModel() {

    private val playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.Default)
    fun observePlayerStare(): LiveData<PlayerState> = playerStateLiveData

    private val handler = Handler(Looper.getMainLooper())
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private var currentTime = START_TIME

    private val timeRunnable = object : Runnable {
        override fun run() {
            if (playerStateLiveData.value is PlayerState.Playing) {
                currentTime = dateFormat.format(mediaPlayer.currentPosition)
                playerStateLiveData.postValue(PlayerState.Playing(currentTime))
            }
            val postTime = SystemClock.uptimeMillis() + TIME_UPDATE_DELAY
            handler.postAtTime(
                this,
                TIME_UPDATE_TOKEN,
                postTime,
            )
        }
    }

    init {
        preparePlayer()
        mediaPlayer.setOnCompletionListener {
            playerStateLiveData.postValue(PlayerState.Finished)
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerStateLiveData.postValue(PlayerState.Prepared)
        }
        mediaPlayer.setOnCompletionListener {
            playerStateLiveData.postValue(PlayerState.Prepared)
        }
    }

    fun playbackControl() {
        when (playerStateLiveData.value) {
            is PlayerState.Playing -> {
                pausePlayer()
            }

            is PlayerState.Prepared,
            is PlayerState.Paused -> {
                startPlayer()
            }

            else -> preparePlayer()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        handler.post(timeRunnable)
        playerStateLiveData.postValue(PlayerState.Playing(currentTime))
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        handler.removeCallbacksAndMessages(TIME_UPDATE_TOKEN)
        playerStateLiveData.postValue(PlayerState.Paused)
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }

    companion object {
        private const val START_TIME = "00:00"
        private val TIME_UPDATE_TOKEN = Any()
        private const val TIME_UPDATE_DELAY = 300L

        fun getFactory(url: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val mediaPlayer = MediaPlayer()
                AudioPlayerViewModel(mediaPlayer, url)
            }
        }
    }


}