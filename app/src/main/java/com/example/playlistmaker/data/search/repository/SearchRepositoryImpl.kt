package com.example.playlistmaker.data.search.repository

import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.domain.search.api.SearchRepository
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.data.mapper.DataMapper
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.domain.search.models.Resource

class SearchRepositoryImpl (private val networkClient: NetworkClient) : SearchRepository {

    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))

        return if (response is TracksSearchResponse) {
            val tracks = DataMapper.map(response.results)
            Resource.Success(tracks)
        } else {
            Resource.Error("Connection Error")
        }
    }
}