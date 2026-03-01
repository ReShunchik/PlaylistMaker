package com.example.playlistmaker.ui.playList.viewModel

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlist.api.ImageInteractor
import com.example.playlistmaker.domain.playlist.api.PlaylistInteractor
import com.example.playlistmaker.domain.playlist.api.TrackPlaylistInteractor
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.sharing.api.SharingInteractor
import kotlinx.coroutines.launch

class TrackPlaylistViewModel(
    private val trackPlaylistInteractor: TrackPlaylistInteractor,
    private val imageInteractor: ImageInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor
): ViewModel() {

    private val tracksStateLiveData = MutableLiveData<TrackPlaylistState>()
    fun observeTracksState(): LiveData<TrackPlaylistState> = tracksStateLiveData

    private val imagePlaylistLiveData = MutableLiveData<Uri?>()
    fun observeImagePlaylist(): LiveData<Uri?> = imagePlaylistLiveData

    private val playlistLiveData = MutableLiveData<Playlist>()
    fun observePlaylist(): LiveData<Playlist> = playlistLiveData

    fun fillData(idList: List<Long>){
        viewModelScope.launch {
            val tracks = trackPlaylistInteractor
                .getTracksById(idList)
            processResult(tracks)
        }
    }

    fun fillImage(name: String){
        viewModelScope.launch {
            val image = imageInteractor.getImage(name)
            if(image == null){
                imagePlaylistLiveData.postValue(null)
            } else {
                imagePlaylistLiveData.postValue(image.toUri())
            }
        }
    }

    fun calculateTime(tracks: List<Track>): String{
        var timeInMillis = 0L
        tracks.forEach {
            timeInMillis += timeStringToMillis(it.trackTime)
        }
        return (timeInMillis/(1000 * 60)).toString()
    }

    fun deleteTrack(playlist: Playlist, trackId: Long){
        viewModelScope.launch {
            playlist.tracks.remove(trackId)
            playlistInteractor.updatePlayList(playlist, null)
            fillData(playlist.tracks)
        }
    }

    fun timeStringToMillis(timeString: String): Long {
        val parts = timeString.split(":")
        if (parts.size != 2) return 0L

        val minutes = parts[0].toLong()
        val seconds = parts[1].toLong()

        return (minutes * 60 + seconds) * 1000
    }

    private fun processResult(tracks: List<Track>){
        if(tracks.isEmpty()){
            renderState(TrackPlaylistState.Empty)
        } else {
            renderState(TrackPlaylistState.Content(
                tracks.reversed(),
                calculateTime(tracks)))
        }
    }

    private fun renderState(state: TrackPlaylistState){
        tracksStateLiveData.postValue(state)
    }

    fun sharePlaylist(playlist: Playlist){
        var message = playlist.name + "\n" + playlist.descriotion + "\n" + playlist.tracks.size + " треков" + "\n"
        var position = 1
        if(tracksStateLiveData.value is TrackPlaylistState.Content){
            val currentState = tracksStateLiveData.value
            if(currentState is TrackPlaylistState.Content){
                currentState.tracks.forEach {
                    message += (position.toString() + ". " + it.artistName)
                    message += " - "
                    message += it.trackName + "("
                    message += it.trackTime + ")"
                    message += "\n"
                    position += 1
                }
            }
        }
        sharingInteractor.sharePlaylist(message)
    }

    fun fillPlaylist(id: Long){
        viewModelScope.launch {
            val playlist = playlistInteractor.getPlaylistById(id)
            playlistLiveData.postValue(playlist)
        }
    }

    fun deletePlaylist(playlist: Playlist){
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(playlist)
        }
    }
}