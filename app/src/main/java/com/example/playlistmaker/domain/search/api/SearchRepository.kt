package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.models.Track

interface SearchRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}