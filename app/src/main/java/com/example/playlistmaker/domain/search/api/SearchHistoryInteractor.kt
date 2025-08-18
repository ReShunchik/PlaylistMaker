package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun freshHistory(track: Track)
    fun clearHistory()
    fun getHistory(): ArrayList<Track>
    //fun saveToHistory(tracks: ArrayList<Track>)
}