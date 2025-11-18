package com.example.playlistmaker.ui.mediaLibrary.viewModel

import com.example.playlistmaker.domain.search.models.Track

interface FavoriteState {

    data class Content(
        val tracks: List<Track>
    ) : FavoriteState

    data class Empty(
        val message: String
    ): FavoriteState
}