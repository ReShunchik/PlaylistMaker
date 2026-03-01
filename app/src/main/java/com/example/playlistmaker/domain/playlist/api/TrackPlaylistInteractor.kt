package com.example.playlistmaker.domain.playlist.api

import com.example.playlistmaker.domain.search.models.Track

interface TrackPlaylistInteractor {

    suspend fun getTracksById(idList: List<Long>): List<Track>
}