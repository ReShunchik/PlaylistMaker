package com.example.playlistmaker.data.dto

data class SavedTrackDto(val trackName: String,
                    val artistName: String,
                    val trackTime: String,
                    val artworkUrl100: String,
                    val country: String,
                    val genre: String,
                    val album: String?,
                    val year: String?,
                    val previewUrl: String
) {
}