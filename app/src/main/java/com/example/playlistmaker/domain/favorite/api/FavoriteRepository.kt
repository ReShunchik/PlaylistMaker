package com.example.playlistmaker.domain.favorite.api

import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    suspend fun insertTrack(track: Track)

    fun getFavoriteTracks(): Flow<List<Track>>

    suspend fun deleteTrack(track: Track)

    suspend fun getTrackById(trackId: Long): Track?
}