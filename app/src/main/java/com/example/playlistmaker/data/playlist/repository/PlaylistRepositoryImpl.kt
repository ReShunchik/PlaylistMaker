package com.example.playlistmaker.data.playlist.repository

import com.example.playlistmaker.data.mapper.PlaylistDbConverter
import com.example.playlistmaker.data.playlist.dao.PlaylistDao
import com.example.playlistmaker.data.playlist.entity.PlaylistEntity
import com.example.playlistmaker.domain.playlist.api.PlaylistRepository
import com.example.playlistmaker.domain.playlist.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistDbConverter: PlaylistDbConverter
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

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDao.updatePlaylist(
            playlistDbConverter.map(playlist)
        )
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