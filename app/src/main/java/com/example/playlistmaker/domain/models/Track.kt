package com.example.playlistmaker.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(val trackName: String,
                 val artistName: String,
                 val trackTime: String,
                 val artworkUrl100: String,
                 val country: String,
                 val genre: String,
                 val album: String?,
                 val year: String?,
                 val previewUrl: String
                 ) : Parcelable