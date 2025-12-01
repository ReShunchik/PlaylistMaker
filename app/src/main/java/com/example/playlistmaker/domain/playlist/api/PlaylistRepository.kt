package com.example.playlistmaker.domain.playlist.api

import com.example.playlistmaker.domain.playlist.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun insertPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

    fun getAllPlaylists(): Flow<List<Playlist>>
}