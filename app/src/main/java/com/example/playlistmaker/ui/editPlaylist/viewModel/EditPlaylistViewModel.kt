package com.example.playlistmaker.ui.editPlaylist.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlist.api.PlaylistInteractor
import com.example.playlistmaker.domain.playlist.models.Playlist
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {

    fun updatePlaylist(playlist: Playlist, uri: Uri?){
        viewModelScope.launch {
            Log.d("Image", "Image save1")
            playlistInteractor.updatePlaylistWithImage(playlist, uri)
        }
    }
}