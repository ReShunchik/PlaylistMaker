package com.example.playlistmaker.adapters

import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.datas.Track
import java.util.Locale
import java.text.SimpleDateFormat


class TrackAdapter(val app: App): RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    private val tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view, app)
    }

    fun updateTracks(newTracks: List<Track>){
        tracks.clear()
        tracks.addAll(newTracks)
        notifyDataSetChanged()
    }

    fun updateHistoryTracks(){
        tracks.clear()
        tracks.addAll(app.readTracks())
        notifyDataSetChanged()
    }

    fun clearTracks(){
        tracks.clear()
        notifyDataSetChanged()
    }

    fun clearHistory(){
        app.clearHistory()
        clearTracks()
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    class TrackViewHolder(val itemView: View, val app: App): RecyclerView.ViewHolder(itemView){
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

        fun bind(track: Track){
            trackName.text = track.trackName
            trackArtist.text = track.artistName
            trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
            Glide.with(itemView.context)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.track_placeholder)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(2f)))
                .into(trackImage)

            itemView.setOnClickListener{
                Log.d("History","start")
                app.freshHistory(track)
                Log.d("History","end")
            }
        }

        fun dpToPx(dp: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                itemView.context.resources.displayMetrics).toInt()
        }
    }
}