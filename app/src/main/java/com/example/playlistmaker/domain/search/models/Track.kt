package com.example.playlistmaker.domain.search.models

import java.io.Serializable

data class Track(val trackName: String,
                 val artistName: String,
                 val trackTime: String,
                 val artworkUrl100: String,
                 val country: String,
                 val genre: String,
                 val album: String?,
                 val year: String?,
                 val previewUrl: String
                 ) : Serializable