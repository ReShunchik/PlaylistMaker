package com.example.playlistmaker.ui.search.viewModel


import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.api.consumer.Consumer
import com.example.playlistmaker.domain.api.consumer.ConsumerData
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.SearchInteractor

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val searchInteractor: SearchInteractor
): ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private var latestSearchText = SEARCH_DEF

    private val history= ArrayList<Track>()
    fun getHistory() = history

    private val trackStateLiveData = MutableLiveData<TracksState>()
    fun observeTrackState(): LiveData<TracksState> = trackStateLiveData

    fun searchRequest() {
        val searchText = latestSearchText
        if(searchText.isNotEmpty()){
            trackStateLiveData.postValue(TracksState.Loading)
        }
        searchInteractor.searchTracks(
            searchText,
            consumer = object : Consumer<List<Track>> {

                override fun consume(data: ConsumerData<List<Track>>) {
                    when (data) {
                        is ConsumerData.Error -> {
                            when (data.message) {
                                CONNECTION_ERROR -> {
                                    trackStateLiveData.postValue(TracksState.Error(data.message))
                                }

                                NO_RESULTS -> {
                                    trackStateLiveData.postValue(TracksState.Empty(data.message))
                                }
                            }
                        }

                        is ConsumerData.Data -> {
                            trackStateLiveData.postValue(TracksState.Content(data.value as ArrayList<Track>))
                        }
                    }

                }
            }
        )
    }

    fun clearHistory(){
        searchHistoryInteractor.clearHistory()
    }

    fun freshHistory(track: Track){
        searchHistoryInteractor.freshHistory(track)
        updateHistory()
    }

    private fun updateHistory(){
        history.clear()
        history.addAll(searchHistoryInteractor.getHistory())
    }

    fun searchDebounce(changedText: String){
        if (latestSearchText.equals(changedText)) {
            return
        }

        latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest() }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
        private const val SEARCH_DEF = ""
        private const val CONNECTION_ERROR = "Connection Error"
        private const val NO_RESULTS = "No results"

        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    Creator.provideSearchHistoryInteractor(),
                    Creator.provideSearchInteractor())
            }
        }
    }

}