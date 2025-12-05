package com.example.playlistmaker.domain.playlist.api

import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun insertPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist, track: Track?)

    suspend fun getPlaylistId(playlistId: Long): Playlist

    fun getAllPlaylists(): Flow<List<Playlist>>
}