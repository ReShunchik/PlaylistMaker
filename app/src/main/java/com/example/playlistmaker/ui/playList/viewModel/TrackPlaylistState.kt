package com.example.playlistmaker.ui.playList.viewModel

import com.example.playlistmaker.domain.search.models.Track

sealed interface TrackPlaylistState {

    data class Content(
        val tracks: List<Track>,
        val time: String
    ): TrackPlaylistState

    object Empty: TrackPlaylistState

}