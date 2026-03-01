package com.example.playlistmaker.domain.playlist.api

import com.example.playlistmaker.domain.search.models.Track

interface TrackPlaylistRepository {

    suspend fun insertTrack(track: Track)

    suspend fun getTrackById(trackId: Long): Track?
}