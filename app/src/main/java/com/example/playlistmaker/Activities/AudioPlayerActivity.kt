package com.example.playlistmaker.Activities

import android.os.Bundle
import android.util.TypedValue
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        setInfo()
        findViewById<ImageView>(R.id.button_back).setOnClickListener{
            finish()
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
            trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
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

    companion object{
        const val TRACK = "track"
    }
}