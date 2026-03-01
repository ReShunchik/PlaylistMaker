package com.example.playlistmaker.ui.playList.adapters

import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.search.adapters.TrackAdapter

class TrackPlaylistAdapter(
    private val onItemClick: (track: Track) -> Unit,
    private val onItemLongClick: (track: Track) -> Unit,
): TrackAdapter(onItemClick) {

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener{
            onItemClick(track)
        }
        holder.itemView.setOnLongClickListener{
            onItemLongClick(track)
            true
        }
    }
}