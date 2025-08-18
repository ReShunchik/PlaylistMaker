package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.data.mapper.DataMapper
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.domain.models.Resource

class TracksRepositoryImpl (private val networkClient: NetworkClient) : TracksRepository {

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