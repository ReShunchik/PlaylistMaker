package com.example.playlistmaker.domain.playlist.impl

import com.example.playlistmaker.domain.playlist.api.TrackPlaylistInteractor
import com.example.playlistmaker.domain.playlist.api.TrackPlaylistRepository
import com.example.playlistmaker.domain.search.models.Track

class TrackPlaylistInteractorImpl(
    private val trackPlaylistRepository: TrackPlaylistRepository
): TrackPlaylistInteractor {

    override suspend fun getTracksById(idList: List<Long>): List<Track> {
        val tracks = mutableListOf<Track>()
        idList.forEach{
            val track = trackPlaylistRepository.getTrackById(it)
            if(track != null){
                tracks.add(track)
            }
        }
        return tracks
    }
}