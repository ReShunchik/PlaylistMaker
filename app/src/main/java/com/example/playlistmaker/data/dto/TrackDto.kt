package com.example.playlistmaker.data.dto

import com.google.gson.annotations.SerializedName


data class TrackDto(val trackName: String,
                 val artistName: String,
                 @SerializedName("trackTimeMillis") val trackTime: Long,
                 val artworkUrl100: String,
                 val country: String,
                 @SerializedName("primaryGenreName") val genre: String,
                 @SerializedName("collectionName") val album: String?,
                 @SerializedName("releaseDate") val year: String?,
                 val previewUrl: String
)