package com.example.playlistmaker.domain.playlist.api

import android.net.Uri
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun insertPlaylist(playlist: Playlist, uri: Uri?)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun updatePlayList(playlist: Playlist, track: Track?)

    suspend fun updatePlaylistWithImage(playlist: Playlist, uri: Uri?)

    fun getAllPlayList(): Flow<List<Playlist>>

}