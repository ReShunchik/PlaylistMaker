package com.example.playlistmaker.data.favorite.repository

import com.example.playlistmaker.data.favorite.FavoriteDatabase
import com.example.playlistmaker.data.favorite.entity.TrackEntity
import com.example.playlistmaker.data.mapper.TrackDbConverter
import com.example.playlistmaker.domain.favorite.api.FavoriteRepository
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val favoriteDatabase: FavoriteDatabase,
    private val trackDbConverter: TrackDbConverter
): FavoriteRepository {

    override suspend fun insertTrack(track: Track) {
        favoriteDatabase.trackDao().insertTrack(
            trackDbConverter.map(track)
        )
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = favoriteDatabase.trackDao().getFavoriteTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun deleteTrack(track: Track) {
        favoriteDatabase.trackDao().deleteTrack(
            trackDbConverter.map(track)
        )
    }

    override suspend fun getTrackById(trackId: Long): Track?{
        val track = favoriteDatabase.trackDao().getTrackById(trackId)
        if(track == null){
            return null
        } else {
            return trackDbConverter.map(track)
        }
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track>{
        return tracks.map { track -> trackDbConverter.map(track) }
    }
}