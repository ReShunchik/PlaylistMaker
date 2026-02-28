package com.example.playlistmaker.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.audioPlayer.viewModel.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin
import java.text.SimpleDateFormat

class MusicService: Service(), AudioPlayerControl {

    private val binder = MusicServiceBinder()

    private var mediaPlayer: MediaPlayer? = null
    private var timerJob: Job? = null
    private var songUrl = ""
    private var contentText = ""

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Default())
    val playerState = _playerState.asStateFlow()

    private val dateFormat: SimpleDateFormat = getKoin().get()

    override fun onBind(intent: Intent?): IBinder? {
        songUrl = intent?.getStringExtra(SONG_URL) ?: ""
        contentText = intent?.getStringExtra(CONTENT_TEXT) ?: ""
        preparePlayer(songUrl)

        createNotificationChannel()

        return binder
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
    }

    private fun preparePlayer(url: String?) {
        mediaPlayer?.setDataSource(url)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            _playerState.value = PlayerState.Prepared()
        }
        mediaPlayer?.setOnCompletionListener {
            timerJob?.cancel()
            _playerState.value = PlayerState.Prepared()
            hideNotification()
        }
    }

    override fun getCurrentPlayerState(): StateFlow<PlayerState> {
        return playerState
    }

    override fun startPlayer() {
        mediaPlayer?.start()
        _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
        startTimer()
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
        timerJob?.cancel()
        _playerState.value = PlayerState.Paused(getCurrentPlayerPosition())
    }

    override fun hideNotification() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE )
    }

    override fun showNotification() {
        ServiceCompat.startForeground(
            this,
            SERVICE_NOTIFICATION_ID,
            createServiceNotification(),
            getForegroundServiceTypeConstant()
        )
    }

    private fun releasePlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        _playerState.value = PlayerState.Default()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (mediaPlayer?.isPlaying == true) {
                delay(TIME_UPDATE_DELAY)
                _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return dateFormat.format(mediaPlayer?.currentPosition) ?: "00:00"
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager?.createNotificationChannel(channel)
    }

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        return super.onUnbind(intent)
    }

    companion object {
        private const val TIME_UPDATE_DELAY = 300L
        private const val SONG_URL = "song_url"
        private const val CONTENT_TEXT = "content_text"

        private const val CHANNEL_NAME = "Music service"
        private const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
        private const val SERVICE_NOTIFICATION_ID = 100
    }

    inner class MusicServiceBinder: Binder(){
        fun getService():MusicService = this@MusicService
    }
}