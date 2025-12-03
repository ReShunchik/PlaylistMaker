package com.example.playlistmaker.data.playlist.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val descriotion: String,
    val tracks: String,
    val imageName: String
)