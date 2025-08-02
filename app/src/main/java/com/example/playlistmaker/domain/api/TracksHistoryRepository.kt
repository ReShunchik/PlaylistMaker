package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.dto.SavedTrackDto
import com.example.playlistmaker.domain.models.Track

interface TracksHistoryRepository {
    fun freshHistory(track: SavedTrackDto)
    fun clearHistory()
    fun readTracks(): Array<SavedTrackDto>
    fun getTracks(): Array<Track>
    fun writeTracks(tracks: ArrayList<SavedTrackDto>)
}