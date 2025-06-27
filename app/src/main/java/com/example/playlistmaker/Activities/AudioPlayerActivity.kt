package com.example.playlistmaker.Activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.Utils.Utils
import com.example.playlistmaker.datas.Track
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        setInfo()
        findViewById<ImageView>(R.id.button_back).setOnClickListener{
            finish()
        }
    }

    private fun setInfo(){
        val track: Track? = intent.getParcelableExtra("track")
        if (track != null){
            val atworkUrl512 = track.artworkUrl100.replace("100x100", "512x512")
            Glide.with(this)
                .load(atworkUrl512)
                .placeholder(R.drawable.track_placeholder_512)
                .centerCrop()
                .transform(RoundedCorners(Utils.dpToPx(this, 8f)))
                .into(findViewById(R.id.atwork))
            findViewById<TextView>(R.id.track_name).text = track.trackName
            findViewById<TextView>(R.id.track_artist).text = track.artistName
            findViewById<TextView>(R.id.duration_info).text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
            findViewById<TextView>(R.id.genre_info).text = track.genre
            findViewById<TextView>(R.id.country_info).text = track.country
            if(track.year != null){
                findViewById<Group>(R.id.year_group).visibility = View.VISIBLE
                findViewById<TextView>(R.id.year_info).text = OffsetDateTime.parse(track.year).year.toString()
            }
            if(track.album != null){
                findViewById<Group>(R.id.album_group).visibility = View.VISIBLE
                findViewById<TextView>(R.id.album_info).text = track.album
            }
        } else {
            finish()
        }
    }
}