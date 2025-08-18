package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.data.dto.SavedTrackDto
import com.example.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    fun freshHistory(track: SavedTrackDto)
    fun clearHistory()
    fun getHistory(): ArrayList<Track>
    fun saveToHistory(tracks: ArrayList<SavedTrackDto>)
}