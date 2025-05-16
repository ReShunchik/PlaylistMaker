package com.example.playlistmaker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.Track

class TrackAdapter(private val tracks: List<Track>,
                   val context: Context): RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], context)
    }

    class TrackViewHolder(parentView: View): RecyclerView.ViewHolder(parentView){
        private val trackName: TextView
        private val trackArtist: TextView
        private val trackTime: TextView
        private val trackImage: ImageView

        init {
            trackName = parentView.findViewById(R.id.track_name)
            trackArtist = parentView.findViewById(R.id.track_artist)
            trackTime = parentView.findViewById(R.id.track_time)
            trackImage = parentView.findViewById(R.id.track_image)
        }

        fun bind(track: Track, context: Context){
            trackName.text = track.trackName
            trackArtist.text = track.artistName
            trackTime.text = track.trackTime
            Glide.with(context)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.track_placeholder)
                .centerCrop()
                .transform(RoundedCorners(10))
                .into(trackImage)
        }
    }
}