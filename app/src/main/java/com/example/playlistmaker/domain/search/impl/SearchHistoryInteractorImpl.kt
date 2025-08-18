package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.SearchHistoryRepository
import com.example.playlistmaker.domain.mapper.DomainMapper
import com.example.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(
    private val tracksHistoryRepository: SearchHistoryRepository
): SearchHistoryInteractor {

    override fun freshHistory(track: Track) {
        tracksHistoryRepository.freshHistory(DomainMapper.map(track))
    }

    override fun clearHistory() {
        tracksHistoryRepository.clearHistory()
    }

    override fun getHistory(): ArrayList<Track> {
        return tracksHistoryRepository.getHistory()
    }

    /*override fun saveToHistory(tracks: ArrayList<Track>) {
        tracksHistoryRepository.saveToHistory(DomainMapper.map(tracks))
    }*/
}