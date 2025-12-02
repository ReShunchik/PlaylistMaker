package com.example.playlistmaker.ui.mediaLibrary.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlist.api.PlaylistInteractor
import com.example.playlistmaker.domain.playlist.models.Playlist
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {

    private val playlistStateLiveData = MutableLiveData<PlaylistState>()
    fun observePlaylistState(): LiveData<PlaylistState> = playlistStateLiveData

    fun fillData(){
        viewModelScope.launch {
            playlistInteractor
                .getAllPlayList()
                .collect{
                        playlists -> processResult(playlists)
                }
        }
    }

    private fun processResult(playlists: List<Playlist>){
        if(playlists.isEmpty()){
            renderState(PlaylistState.Empty)
        } else {
            renderState(PlaylistState.Content(playlists))
        }
    }

    private fun renderState(state: PlaylistState){
        playlistStateLiveData.postValue(state)
    }

}