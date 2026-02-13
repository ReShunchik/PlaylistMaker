package com.example.playlistmaker.ui.search.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.SearchInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val searchInteractor: SearchInteractor,
): ViewModel() {
    private var latestSearchText = SEARCH_DEF

    private var searchJob: Job? = null

    private val _trackState = MutableStateFlow<TracksState>(TracksState.Default)
    val trackState: StateFlow<TracksState> = _trackState.asStateFlow()

    private val _trackHistory = MutableStateFlow<ArrayList<Track>>(getHistory())
    val trackHistory: StateFlow<ArrayList<Track>> = _trackHistory.asStateFlow()

    fun searchRequest() {
        val searchText = latestSearchText
        if(searchText.isNotEmpty()){
            _trackState.value = TracksState.Loading

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
            _trackState.value = TracksState.Error(message)
        } else {
            if (tracks.isNullOrEmpty()){
                val history: ArrayList<Track>?
                if(latestSearchText.isNullOrEmpty()){
                    history = searchHistoryInteractor.getHistory()
                } else {
                    history = null
                }
                _trackState.value = TracksState.Empty(
                    message ?: "")
            } else {
                _trackState.value = TracksState.Content(tracks as ArrayList<Track>)
            }
        }
    }

    fun getHistory(): ArrayList<Track>{
        return searchHistoryInteractor.getHistory()
    }

    fun clearHistory(){
        searchHistoryInteractor.clearHistory()
        _trackState.value = TracksState.Default
    }

    fun freshHistory(track: Track){
        searchHistoryInteractor.freshHistory(track)
        _trackHistory.value = getHistory()
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

    fun clearSearch(){
        _trackState.value = TracksState.History
        _trackHistory.value = getHistory()
    }

    fun setDefault(){
        _trackState.value = TracksState.Default
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val SEARCH_DEF = ""
    }

}