package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.dto.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    @GET("search")
    suspend fun search(
        @Query("term") term: String,
        @Query("entity") entity: String = "musicTrack",
        @Query("limit") limit: Int = 25
    ): TracksSearchResponse

}