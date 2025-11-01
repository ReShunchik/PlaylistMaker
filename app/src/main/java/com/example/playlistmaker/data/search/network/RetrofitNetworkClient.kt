package com.example.playlistmaker.data.search.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val api: ITunesApi,
    private val context: Context
): NetworkClient {

    override suspend fun doRequest(searchRequest: Any): Response {
        if (isConnected() == false){
            return Response().apply { resultCode = -1 }
        }

        if (searchRequest !is TracksSearchRequest){
            return Response().apply { resultCode = 400 }
        }

        return try {
                val networkResponse = api.search(searchRequest.request)
                networkResponse.apply { resultCode = 200 }
            } catch (ex: Exception){
                Response().apply { resultCode = 500 }
            }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}