package com.example.playlistmaker.data.playlist.repository

import com.example.playlistmaker.data.mapper.PlaylistDbConverter
import com.example.playlistmaker.data.mapper.TrackPlaylistDbConverter
import com.example.playlistmaker.data.playlist.dao.PlaylistDao
import com.example.playlistmaker.data.playlist.dao.TrackPlaylistDao
import com.example.playlistmaker.data.playlist.entity.PlaylistEntity
import com.example.playlistmaker.domain.playlist.api.PlaylistRepository
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val trackPlaylistDao: TrackPlaylistDao,
    private val playlistDbConverter: PlaylistDbConverter,
    private val trackPlaylistDbConverter: TrackPlaylistDbConverter
): PlaylistRepository {

    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistDao.insertPlaylist(
            playlistDbConverter.map(playlist)
        )
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistDao.deletePlaylist(
            playlistDbConverter.map(playlist)
        )
    }

    override suspend fun updatePlaylist(playlist: Playlist, track: Track?) {
        playlistDao.updatePlaylist(
            playlistDbConverter.map(playlist)
        )
        if(track != null){
            trackPlaylistDao.insertTrack(
                trackPlaylistDbConverter.map(track)
            )
        }
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> = flow{
        val playlists = playlistDao.getAllPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist>{
        return playlists.map {
            playlist -> playlistDbConverter.map(playlist)
        }
    }
}