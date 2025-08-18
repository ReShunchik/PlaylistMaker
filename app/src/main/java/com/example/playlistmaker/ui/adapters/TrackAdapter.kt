package com.example.playlistmaker.ui.adapters

import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.ui.audioPlayer.activity.AudioPlayerActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.search.viewModel.SearchViewModel


class TrackAdapter(
    private val context: Context,
    private val viewModel: SearchViewModel
): RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    private val tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder.from(parent)

    fun updateTracks(newTracks: List<Track>){
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
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener{
            viewModel.freshHistory(track)
            val intent = Intent(context, AudioPlayerActivity::class.java)
            intent.putExtra(TRACK, track)
            context.startActivity(intent)
        }
    }

    class TrackViewHolder(private val binding: TrackViewBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(track: Track){
            binding.trackName.text = track.trackName
            binding.trackArtist.text = track.artistName
            binding.trackTime.text = track.trackTime
            Glide.with(itemView.context)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.track_placeholder_100)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(2f)))
                .into(binding.trackImage)
        }

        fun dpToPx(dp: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                itemView.context.resources.displayMetrics).toInt()
        }

        companion object{
            fun from(parent: ViewGroup, ): TrackViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = TrackViewBinding.inflate(inflater, parent, false)
                return TrackViewHolder(binding)
            }
        }
    }

    companion object{
        const val TRACK = "track"
    }
}