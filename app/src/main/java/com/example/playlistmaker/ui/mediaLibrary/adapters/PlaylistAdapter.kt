package com.example.playlistmaker.ui.mediaLibrary.adapters

import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistView1Binding
import com.example.playlistmaker.domain.playlist.api.ImageInteractor
import com.example.playlistmaker.domain.playlist.models.Playlist
import org.koin.core.component.KoinComponent

class PlaylistAdapter(
    private val onItemClick: (playlist: Playlist) -> Unit
): RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>(), KoinComponent {

    private val imageInteractor: ImageInteractor = getKoin().get<ImageInteractor>()

    private val playlists = ArrayList<Playlist>()

    fun updatePlaylists(newPlaylists: List<Playlist>){
        playlists.clear()
        playlists.addAll(newPlaylists)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder =
        PlaylistViewHolder.from(parent)

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        val playlistImage = imageInteractor.getImage(playlist.imageName)?.toUri()
        holder.bind(playlist, playlistImage)
        holder.itemView.setOnClickListener{
            onItemClick(playlist)
        }
    }

    class PlaylistViewHolder(
        private val binding: PlaylistView1Binding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(playlist: Playlist, uri: Uri?) {
            binding.playlistName.text = playlist.name
            binding.playlistTrackCount.text = chooseCase(playlist.tracks.size)
            Glide.with(itemView.context)
                .load(uri)
                .placeholder(R.drawable.track_placeholder_100)
                .transform(CenterCrop(), RoundedCorners(dpToPx(8f)))
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
            fun from(parent: ViewGroup): PlaylistViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = PlaylistView1Binding.inflate(inflater, parent, false)
                return PlaylistViewHolder(binding)
            }
        }
    }

}
