package com.example.playlistmaker.ui.search.adapters

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.domain.search.models.Track


open class TrackAdapter(
    private val onItemClick: (track: Track) -> Unit
): RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    protected val tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder.from(parent)

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
            onItemClick(track)
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

}