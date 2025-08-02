package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TracksSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    @GET("search")
    fun search(
        @Query("term") term: String,
        @Query("entity") entity: String = "musicTrack",
        @Query("limit") limit: Int = 25
    ): Call<TracksSearchResponse>

}