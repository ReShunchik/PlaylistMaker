package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TracksHistoryInteractor
import com.example.playlistmaker.domain.api.TracksHistoryRepository
import com.example.playlistmaker.domain.mapper.DomainMapper
import com.example.playlistmaker.domain.models.Track

class TracksHistoryInteractorImpl(
    private val tracksHistoryRepository: TracksHistoryRepository
): TracksHistoryInteractor {
    override fun freshHistory(track: Track) {
        tracksHistoryRepository.freshHistory(DomainMapper.map(track))
    }

    override fun clearHistory() {
        tracksHistoryRepository.clearHistory()
    }

    override fun getTracks(): Array<Track> {
        return tracksHistoryRepository.getTracks()
    }

    override fun writeTracks(tracks: ArrayList<Track>) {
        tracksHistoryRepository.writeTracks(DomainMapper.map(tracks))
    }
}