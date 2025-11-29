package com.example.playlistmaker.data.favorite.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
data class TrackEntity(
    @PrimaryKey
    val id: Long,
    val position: Long,
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