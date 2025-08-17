package com.example.playlistmaker.ui.search.viewModel

import com.example.playlistmaker.domain.models.Track

sealed interface TracksState {

    object Loading : TracksState

    data class Content(
        val tracks: ArrayList<Track>
    ) : TracksState

    data class Error(
        val message: String
    ) : TracksState

    data class Empty(
        val message: String
    ) : TracksState
}