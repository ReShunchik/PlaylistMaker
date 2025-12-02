package com.example.playlistmaker.ui.audioPlayer.adapters

import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistView2Binding
import com.example.playlistmaker.domain.playlist.api.ImageInteractor
import com.example.playlistmaker.domain.playlist.api.PlaylistInteractor
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class BottomSheetAdapter(
    private val track: Track,
    private val onItemClickUI: (message: String) -> Unit,
    private val onItemClickDb: (playlist: Playlist, track: Track?) -> Unit,
    private val getPlaylistImage: (playlistName: String) -> Uri?
) :RecyclerView.Adapter<BottomSheetAdapter.BottomSheetViewHolder>(), KoinComponent {

    private val playlists = ArrayList<Playlist>()

    fun setPlaylists(playlists: List<Playlist>){
        this.playlists.addAll(playlists)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder =
        BottomSheetViewHolder.from(parent)


    override fun getItemCount(): Int =
        playlists.size

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        val playlist = playlists[position]
        val playlistImage = getPlaylistImage(playlist.name)
        holder.bind(playlist, playlistImage)
        holder.itemView.setOnClickListener{
            val message: String
            if(track.trackId in playlist.tracks){
                message =
                    holder.itemView.context.getString(R.string.yet_added) + " " + playlist.name
                onItemClickDb(playlist, null)
            } else {
                playlist.tracks.add(track.trackId)
                message =
                    holder.itemView.context.getString(R.string.added_to_playlist) + " " + playlist.name
                onItemClickDb(playlist, track)
            }
            onItemClickUI(message)
        }
    }

    class BottomSheetViewHolder(
        private val binding: PlaylistView2Binding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(playlist: Playlist, uri: Uri?) {
            binding.playlistName.text = playlist.name
            binding.tracksCount.text = chooseCase(playlist.tracks.size)
            Glide.with(itemView.context)
                .load(uri)
                .placeholder(R.drawable.track_placeholder_100)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(2f)))
                .into(binding.playlistImage)
        }

        private fun chooseCase(count: Int): String{
            val case: String
            if(count == 1){
                case = itemView.context.getString(R.string.track1)
            } else if ((count >= 2) and (count <= 4)) {
                case = itemView.context.getString(R.string.track2)
            } else {
                case = itemView.context.getString(R.string.track3)
            }
            return count.toString() + " " + case
        }

        fun dpToPx(dp: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                itemView.context.resources.displayMetrics).toInt()
        }

        companion object{
            fun from(parent: ViewGroup): BottomSheetViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = PlaylistView2Binding.inflate(inflater, parent, false)
                return BottomSheetViewHolder(binding)
            }
        }
    }
}