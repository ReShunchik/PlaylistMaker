package com.example.playlistmaker.datas

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(val trackName: String,
                 val artistName: String,
                 @SerializedName("trackTimeMillis") val trackTime: Long,
                 val artworkUrl100: String,
                 val country: String,
                 @SerializedName("primaryGenreName") val genre: String,
                 @SerializedName("collectionName") val album: String?,
                 @SerializedName("releaseDate") val year: String?
                 ) : Parcelable