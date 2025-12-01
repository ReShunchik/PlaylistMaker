package com.example.playlistmaker.ui.search.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.SearchInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val searchInteractor: SearchInteractor,
): ViewModel() {



    private var latestSearchText = SEARCH_DEF

    private var searchJob: Job? = null

    private val trackStateLiveData = MutableLiveData<TracksState>()
    fun observeTrackState(): LiveData<TracksState> = trackStateLiveData

    fun searchRequest() {
        val searchText = latestSearchText
        if(searchText.isNotEmpty()){
            trackStateLiveData.postValue(TracksState.Loading)

            viewModelScope.launch {
                searchInteractor
                    .searchTracks(searchText)
                    .collect{
                        pair -> proccesResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun proccesResult(tracks: List<Track>?, message: String?){
        if (message != null){
            trackStateLiveData.postValue(TracksState.Error(message))
        } else {
            if (tracks.isNullOrEmpty()){
                val history: ArrayList<Track>?
                if(latestSearchText.isNullOrEmpty()){
                    history = searchHistoryInteractor.getHistory()
                } else {
                    history = null
                }
                trackStateLiveData.postValue(TracksState.Empty(
                    message ?: "",
                    history))
            } else {
                trackStateLiveData.postValue(TracksState.Content(tracks as ArrayList<Track>))
            }
        }
    }

    fun clearHistory(){
        searchHistoryInteractor.clearHistory()
    }

    fun freshHistory(track: Track){
        searchHistoryInteractor.freshHistory(track)
    }

    fun searchDebounce(changedText: String){
        if (latestSearchText.equals(changedText)) {
            return
        }

        latestSearchText = changedText

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest()
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val SEARCH_DEF = ""
    }

}