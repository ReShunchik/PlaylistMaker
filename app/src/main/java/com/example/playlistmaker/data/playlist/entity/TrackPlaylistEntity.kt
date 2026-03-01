package com.example.playlistmaker.data.playlist.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_playlist_table")
data class TrackPlaylistEntity (
    @PrimaryKey
    val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
    val country: String,
    val genre: String,
    val album: String?,
    val year: String?,
    val previewUrl: String = ""
)