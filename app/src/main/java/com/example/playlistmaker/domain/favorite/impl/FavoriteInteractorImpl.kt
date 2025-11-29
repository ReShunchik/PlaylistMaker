package com.example.playlistmaker.domain.favorite.impl

import com.example.playlistmaker.domain.favorite.api.FavoriteInteractor
import com.example.playlistmaker.domain.favorite.api.FavoriteRepository
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(
    private val favoriteRepository: FavoriteRepository
): FavoriteInteractor {

    override suspend fun insertTrack(track: Track) {
        favoriteRepository.insertTrack(track)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteRepository.getFavoriteTracks()
    }

    override suspend fun deleteTrack(track: Track) {
        favoriteRepository.deleteTrack(track)
    }

    override suspend fun getTrackById(trackId: Long): Track? {
        return favoriteRepository.getTrackById(trackId)
    }

}