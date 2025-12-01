package com.example.playlistmaker.domain.playlist.models

data class Playlist(
    val id: Long,
    val name: String,
    val descriotion: String,
    val tracks: ArrayList<Long>,
    val count: Long
)
