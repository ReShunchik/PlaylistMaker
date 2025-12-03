package com.example.playlistmaker.data.playlist.repository

import com.example.playlistmaker.data.mapper.TrackPlaylistDbConverter
import com.example.playlistmaker.data.playlist.dao.TrackPlaylistDao
import com.example.playlistmaker.domain.playlist.api.TrackPlaylistRepository
import com.example.playlistmaker.domain.search.models.Track

class TrackPlaylistRepositoryImpl(
    private val trackPlaylistDao: TrackPlaylistDao,
    private val trackPlaylistDbConverter: TrackPlaylistDbConverter
): TrackPlaylistRepository {

    override suspend fun insertTrack(track: Track) {
        trackPlaylistDao.insertTrack(
            trackPlaylistDbConverter.map(track)
        )
    }

    override suspend fun getTrackById(trackId: Long): Track? {
        val track = trackPlaylistDao.getTrackById(trackId)
        if(track == null){
            return null
        } else {
            return trackPlaylistDbConverter.map(track)
        }
    }
}