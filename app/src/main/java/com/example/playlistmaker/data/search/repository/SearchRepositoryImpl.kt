package com.example.playlistmaker.data.search.repository

import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.data.mapper.DataMapper
import com.example.playlistmaker.domain.search.api.SearchRepository
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.domain.search.models.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl (private val networkClient: NetworkClient) : SearchRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow{
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when(response.resultCode){
            -1 -> {
                emit(Resource.Error("Сonnection error"))
            }
            200 -> {
                with(response as TracksSearchResponse){
                    val tracks = DataMapper.map(response.results)
                    emit(Resource.Success(tracks))
                }
            }
            else -> {
                emit(Resource.Error("Server error"))
            }
        }
    }
}