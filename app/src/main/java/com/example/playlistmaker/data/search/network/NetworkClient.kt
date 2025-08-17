package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TracksSearchRequest

interface NetworkClient {

    fun doRequest(searchRequest: TracksSearchRequest): Response

}