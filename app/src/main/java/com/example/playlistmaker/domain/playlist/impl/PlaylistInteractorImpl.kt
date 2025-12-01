package com.example.playlistmaker.domain.playlist.impl

import android.net.Uri
import com.example.playlistmaker.domain.playlist.api.ImageRepository
import com.example.playlistmaker.domain.playlist.api.PlaylistInteractor
import com.example.playlistmaker.domain.playlist.api.PlaylistRepository
import com.example.playlistmaker.domain.playlist.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
    private val imageRepository: ImageRepository
): PlaylistInteractor {

    override suspend fun insertPlaylist(playlist: Playlist, uri: Uri?) {
        playlistRepository.insertPlaylist(playlist)
        if(uri != null){
            imageRepository.saveImage(playlist.name, uri)
        }
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistRepository.deletePlaylist(playlist)
    }

    override suspend fun updatePlayList(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

    override fun getAllPlayList(): Flow<List<Playlist>> {
        return playlistRepository.getAllPlaylists()
    }
}