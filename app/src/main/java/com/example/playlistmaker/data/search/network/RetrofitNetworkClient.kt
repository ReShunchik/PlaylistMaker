package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TracksSearchRequest

class RetrofitNetworkClient(
    private val api: ITunesApi
): NetworkClient {

    override fun doRequest(searchRequest: TracksSearchRequest): Response {
        return try {
            val response = api.search(searchRequest.request).execute()
            val networkResponse = response.body() ?: Response()

            networkResponse.apply { resultCode = response.code() }
        } catch (ex: Exception){
            Response().apply { resultCode = -1 }
        }
    }
}