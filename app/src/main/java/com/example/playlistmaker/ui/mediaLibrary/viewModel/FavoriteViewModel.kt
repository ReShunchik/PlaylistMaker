package com.example.playlistmaker.ui.mediaLibrary.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.favorite.api.FavoriteInteractor
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val context: Context,
    private val favoriteInteractor: FavoriteInteractor
): ViewModel() {

    private val favoriteStateLiveData = MutableLiveData<FavoriteState>()
    fun observeFavoriteState(): LiveData<FavoriteState> = favoriteStateLiveData

    fun fillData(){
        viewModelScope.launch {
            favoriteInteractor
                .getFavoriteTracks()
                .collect{ tracks ->
                    processResult(tracks)
                }
        }
    }

    private fun processResult(tracks: List<Track>){
        if(tracks.isEmpty()){
            renderState(FavoriteState.Empty(context.getString(R.string.no_favourite)))
        } else {
            renderState(FavoriteState.Content(tracks))
        }
    }

    private fun renderState(state: FavoriteState){
        favoriteStateLiveData.postValue(state)
    }
}