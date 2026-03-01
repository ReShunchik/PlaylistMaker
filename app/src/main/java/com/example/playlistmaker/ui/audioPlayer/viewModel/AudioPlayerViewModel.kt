package com.example.playlistmaker.ui.audioPlayer.viewModel

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favorite.api.FavoriteInteractor
import com.example.playlistmaker.domain.playlist.api.ImageInteractor
import com.example.playlistmaker.domain.playlist.api.PlaylistInteractor
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.services.AudioPlayerControl
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val favoriteInteractor: FavoriteInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val imageInteractor: ImageInteractor
) : ViewModel() {

    private val playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePlayerStateLiveData(): LiveData<PlayerState> = playerStateLiveData

    private val trackStateLivaData = MutableLiveData<TrackState>()
    fun observeTrackStateLiveData(): LiveData<TrackState> = trackStateLivaData

    fun getCurrentTrackState(): TrackState{
        return trackStateLivaData.value ?: TrackState.NotFavorite
    }

    private val playlistsLiveData = MutableLiveData<List<Playlist>>()
    fun observePlaylistsLiveData(): LiveData<List<Playlist>> = playlistsLiveData

    val onItemClickDb: (playlist: Playlist, track: Track?) -> Unit = {
        playlist, track ->
        viewModelScope.launch {
            playlistInteractor.updatePlayList(playlist, track)
        }
    }

    val getPlaylistImage: (playlistName: String) -> Uri? = {
        playlistName ->
        imageInteractor.getImage(playlistName)?.toUri() ?: null
    }

    private var audioPlayerControl: AudioPlayerControl? = null

    fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl) {
        this.audioPlayerControl = audioPlayerControl

        viewModelScope.launch {
            audioPlayerControl.getCurrentPlayerState().collect {
                playerStateLiveData.postValue(it)
            }
        }
    }

    fun onPlayerButtonClicked() {
        if (playerStateLiveData.value is PlayerState.Playing) {
            audioPlayerControl?.pausePlayer()
        }
        else if (playerStateLiveData.value is PlayerState.Default){
            return
        }
        else {
            audioPlayerControl?.startPlayer()
        }
    }

    fun hideNotification(){
        audioPlayerControl?.hideNotification()
    }

    fun showNotification(){
        if(playerStateLiveData.value is PlayerState.Playing){
            audioPlayerControl?.showNotification()
        }
    }

    fun removeAudioPlayerControl() {
        audioPlayerControl = null
    }

    fun fillData(){
        viewModelScope.launch {
            playlistInteractor
                .getAllPlayList()
                .collect{
                    playlistsLiveData.postValue(it)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerControl = null
    }

    fun addToFavorite(track: Track){
        viewModelScope.launch {
            favoriteInteractor.insertTrack(track)
            trackStateLivaData.postValue(TrackState.IsFavorite)
        }
    }

    fun checkIsFavoriteTrack(trackId: Long){
        viewModelScope.launch {
            val track = favoriteInteractor.getTrackById(trackId)
            if(track == null){
                trackStateLivaData.postValue(TrackState.NotFavorite)
            } else {
                trackStateLivaData.postValue(TrackState.IsFavorite)
            }
        }
    }

    fun deleteTrackFromFavorite(track: Track){
        viewModelScope.launch {
            favoriteInteractor.deleteTrack(track)
            trackStateLivaData.postValue(TrackState.NotFavorite)
        }
    }
}