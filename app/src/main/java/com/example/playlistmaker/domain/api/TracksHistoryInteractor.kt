package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TracksHistoryInteractor {
    fun freshHistory(track: Track)
    fun clearHistory()
    fun getTracks(): Array<Track>
    fun writeTracks(tracks: ArrayList<Track>)
}