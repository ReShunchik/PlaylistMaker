package com.example.playlistmaker.Activities

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.datas.Track
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var playButton: ImageButton
    private lateinit var currentTrackTime: TextView

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    private val timeRunnable = object : Runnable {
        override fun run(){
            if (playerState == STATE_PLAYING) {
                val currentTime = dateFormat.format(mediaPlayer.currentPosition)
                currentTrackTime.setText(currentTime)
            }

            handler.postDelayed(this, TIME_UPDATE_DELAY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        playButton = findViewById(R.id.player_button)
        currentTrackTime = findViewById(R.id.current_time)

        setInfo()

        findViewById<ImageView>(R.id.button_back).setOnClickListener{
            finish()
        }

        playButton.setOnClickListener{
            playbackControl()
        }
    }

    private fun setInfo(){
        val track: Track? = intent.getParcelableExtra(TRACK)
        if (track != null){
            val atworkUrl512 = track.artworkUrl100.replace("100x100", "512x512")
            val atwork = findViewById<ImageView>(R.id.atwork)
            Glide.with(this)
                .load(atworkUrl512)
                .placeholder(R.drawable.track_placeholder_512)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(8f)))
                .into(atwork)
            val trackName = findViewById<TextView>(R.id.track_name)
            trackName.text = track.trackName
            val artistName = findViewById<TextView>(R.id.track_artist)
            artistName.text = track.artistName
            val trackTime = findViewById<TextView>(R.id.duration_info)
            trackTime.text = dateFormat.format(track.trackTime)
            val trackGenre = findViewById<TextView>(R.id.genre_info)
            trackGenre.text = track.genre
            val trackCountry = findViewById<TextView>(R.id.country_info)
            trackCountry.text = track.country
            if(track.year != null){
                val yearGroup = findViewById<Group>(R.id.year_group)
                yearGroup.visibility = View.VISIBLE
                val yearInfo = findViewById<TextView>(R.id.year_info)
                yearInfo.text = OffsetDateTime.parse(track.year).year.toString()
            }
            if(track.album != null){
                val albumGroup = findViewById<Group>(R.id.album_group)
                albumGroup.visibility = View.VISIBLE
                val albumInfo = findViewById<TextView>(R.id.album_info)
                albumInfo.text = track.album
            }
            preparePlayer(track.previewUrl)
        } else {
            finish()
        }
    }

    fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            this.resources.displayMetrics).toInt()
    }

    private fun preparePlayer(previewUrl: String) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.ic_play_84)
            currentTrackTime.setText(R.string.start_track_time)
            playerState = STATE_PREPARED
        }
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        handler.post(timeRunnable)
        playButton.setImageResource(R.drawable.ic_pause_84)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        handler.removeCallbacks(timeRunnable)
        playButton.setImageResource(R.drawable.ic_play_84)
        playerState = STATE_PAUSED
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    companion object{
        const val TRACK = "track"
        private const val TIME_UPDATE_DELAY = 300L
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}