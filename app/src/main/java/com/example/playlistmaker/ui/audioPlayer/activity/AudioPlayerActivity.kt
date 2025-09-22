package com.example.playlistmaker.ui.audioPlayer.activity

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.audioPlayer.viewModel.AudioPlayerViewModel
import com.example.playlistmaker.ui.audioPlayer.viewModel.PlayerState
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf
import java.time.OffsetDateTime

class AudioPlayerActivity : AppCompatActivity(), KoinComponent {

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var viewModel: AudioPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setInfo()

        binding.buttonBack.setOnClickListener{
            finish()
        }

        binding.playerButton.setOnClickListener{
            viewModel.playbackControl()
        }

        viewModel.observePlayerStare().observe(this){
            when(it){
                is PlayerState.Playing -> {
                    showPlaying()
                    binding.currentTime.setText(it.currentTime)
                }
                is PlayerState.Paused -> showPaused()
                is PlayerState.Default -> showDefault()
                is PlayerState.Prepared -> showPrepared()
                is PlayerState.Finished -> {
                    binding.currentTime.setText(TIME_DEFAULT)
                }
            }
        }
    }

    private fun setInfo(){
        val track: Track? = intent.getSerializableExtra(TRACK) as Track
        if (track != null){
            val atworkUrl512 = track.artworkUrl100.replace("100x100", "512x512")
            Glide.with(this)
                .load(atworkUrl512)
                .placeholder(R.drawable.track_placeholder_512)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(8f)))
                .into(binding.atwork)

            binding.trackName.text = track.trackName
            binding.trackArtist.text = track.artistName
            binding.durationInfo.text = track.trackTime
            binding.genreInfo.text = track.genre
            binding.countryInfo.text = track.country
            if(track.year != null){
                binding.yearGroup.visibility = View.VISIBLE
                binding.yearInfo.text = OffsetDateTime.parse(track.year).year.toString()
            }
            if(track.album != null){
                binding.albumGroup.visibility = View.VISIBLE
                binding.albumInfo.text = track.album
            }
            viewModel = getViewModel { parametersOf(track.previewUrl) }
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

    private fun showPlaying(){
        binding.playerButton.setImageResource(R.drawable.ic_pause_84)
    }

    private fun showPaused(){
        binding.playerButton.setImageResource(R.drawable.ic_play_84)
    }

    private fun showPrepared(){
        binding.playerButton.isEnabled = true
        showPaused()
    }

    private fun showDefault(){
        binding.playerButton.setImageResource(R.drawable.ic_play_84)
        binding.playerButton.isEnabled = false
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    companion object{
        private const val TRACK = "track"
        private val TIME_DEFAULT = R.string.start_track_time
    }
}