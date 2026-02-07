package com.example.playlistmaker.ui.audioPlayer.viewModel

import android.media.MediaPlayer
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class AudioPlayerViewModel(
    private val mediaPlayer: MediaPlayer,
    private val dateFormat: SimpleDateFormat,
    private val favoriteInteractor: FavoriteInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val imageInteractor: ImageInteractor
) : ViewModel() {

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