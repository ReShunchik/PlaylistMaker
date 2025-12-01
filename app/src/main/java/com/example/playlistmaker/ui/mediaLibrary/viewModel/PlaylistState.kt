package com.example.playlistmaker.ui.mediaLibrary.viewModel

import com.example.playlistmaker.domain.playlist.models.Playlist

sealed interface PlaylistState {

    data class Content(
        val playlists: List<Playlist>
    ): PlaylistState

    object Empty: PlaylistState
}