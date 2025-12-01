package com.example.playlistmaker.domain.playlist.api

import android.net.Uri
import com.example.playlistmaker.domain.playlist.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun insertPlaylist(playlist: Playlist, uri: Uri?)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun updatePlayList(playlist: Playlist)

    fun getAllPlayList(): Flow<List<Playlist>>

}