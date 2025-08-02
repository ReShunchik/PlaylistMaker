package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.api.consumer.Consumer
import com.example.playlistmaker.domain.api.consumer.ConsumerData
import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.models.Track
import java.util.concurrent.Executors

class TracksInteractorImpl (private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: Consumer<List<Track>>) {
        executor.execute {
            val tracksResource = repository.searchTracks(expression)
            if (tracksResource == null){
                consumer.consume(ConsumerData.Error("No results"))
            } else {
                when(tracksResource){
                    is Resource.Error -> {
                        consumer.consume(ConsumerData.Error(tracksResource.message))
                    }

                    is Resource.Success -> {
                        consumer.consume(ConsumerData.Data(tracksResource.data))
                    }
                }
            }
        }
    }
}