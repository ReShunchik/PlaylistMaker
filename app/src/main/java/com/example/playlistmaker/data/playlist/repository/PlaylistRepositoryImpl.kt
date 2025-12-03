package com.example.playlistmaker.data.playlist.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.playlistmaker.data.mapper.PlaylistDbConverter
import com.example.playlistmaker.data.playlist.dao.PlaylistDao
import com.example.playlistmaker.data.playlist.entity.PlaylistEntity
import com.example.playlistmaker.domain.playlist.api.PlaylistRepository
import com.example.playlistmaker.domain.playlist.api.TrackPlaylistRepository
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val trackPlaylistRepository: TrackPlaylistRepository,
    private val playlistDbConverter: PlaylistDbConverter,
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
        Log.d("Image", "Image checking 1.1")
        playlistDao.updatePlaylist(
            playlistDbConverter.map(playlist)
        )
        Log.d("Image", "Image checking 1.2")
        if(track != null){
            trackPlaylistRepository.insertTrack(track)
        }
        Log.d("Image", "Image checking 1.3")
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

    companion object{
        const val CATALOG = "playlistalbum"
        const val EXTENSION = ".jpg"
    }
}