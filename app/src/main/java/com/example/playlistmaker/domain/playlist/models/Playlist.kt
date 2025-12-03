package com.example.playlistmaker.domain.playlist.models

import java.io.Serializable

data class Playlist(
    val id: Long,
    val name: String,
    val descriotion: String,
    val tracks: ArrayList<Long>,
    val imageName: String
): Serializable
