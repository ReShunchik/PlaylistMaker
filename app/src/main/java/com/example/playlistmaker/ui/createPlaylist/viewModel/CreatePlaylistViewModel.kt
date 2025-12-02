package com.example.playlistmaker.ui.createPlaylist.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlist.api.PlaylistInteractor
import com.example.playlistmaker.domain.playlist.models.Playlist
import kotlinx.coroutines.launch
import java.util.ArrayList

class CreatePlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {

    fun createPlaylist(name: String, description: String, uri: Uri?){
        viewModelScope.launch {
            val playlist = Playlist(
                0L,
                name,
                description,
                ArrayList(),
            )
            playlistInteractor.insertPlaylist(playlist, uri)
        }
    }
}