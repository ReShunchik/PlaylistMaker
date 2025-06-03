package com.example.playlistmaker.adapters

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.datas.TracksResponse
import java.util.Locale
import java.text.SimpleDateFormat


class TrackAdapter(): RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    private val tracks = ArrayList<TracksResponse.Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    fun updateTracks(newTracks: List<TracksResponse.Track>){
        tracks.clear()
        tracks.addAll(newTracks)
        notifyDataSetChanged()
    }

    fun clearTracks(){
        tracks.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val trackName: TextView
        private val trackArtist: TextView
        private val trackTime: TextView
        private val trackImage: ImageView

        init {
            trackName = itemView.findViewById(R.id.track_name)
            trackArtist = itemView.findViewById(R.id.track_artist)
            trackTime = itemView.findViewById(R.id.track_time)
            trackImage = itemView.findViewById(R.id.track_image)
        }

        fun bind(track: TracksResponse.Track){
            trackName.text = track.trackName
            trackArtist.text = track.artistName
            trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
            Glide.with(itemView.context)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.track_placeholder)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(2f)))
                .into(trackImage)
        }

        fun dpToPx(dp: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                itemView.context.resources.displayMetrics).toInt()
        }
    }
}