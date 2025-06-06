package com.example.playlistmaker.datas

import com.google.gson.annotations.SerializedName

class TracksResponse(val resultCount: Int,
                     val results: List<Track>){

    data class Track(val trackName: String,
                     val artistName: String,
                     @SerializedName("trackTimeMillis") val trackTime: Long,
                     val artworkUrl100: String)

}
