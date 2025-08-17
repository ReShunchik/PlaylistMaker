package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.api.consumer.Consumer
import com.example.playlistmaker.domain.models.Track

interface SearchInteractor {

    fun searchTracks(expression: String, consumer: Consumer<List<Track>>)

}