package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.SearchInteractor
import com.example.playlistmaker.domain.search.api.SearchRepository
import com.example.playlistmaker.domain.api.consumer.Consumer
import com.example.playlistmaker.domain.api.consumer.ConsumerData
import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.models.Track
import java.util.concurrent.Executors

class SearchInteractorImpl (private val repository: SearchRepository) : SearchInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: Consumer<List<Track>>) {
        executor.execute {
            val tracksResource = repository.searchTracks(expression)
            if (tracksResource == null){
                consumer.consume(ConsumerData.Error(NO_RESULT))
            } else {
                when(tracksResource){
                    is Resource.Error -> {
                        consumer.consume(ConsumerData.Error(tracksResource.message))
                    }

                    is Resource.Success -> {
                        if(tracksResource.data.size == 0){
                            consumer.consume(ConsumerData.Error(NO_RESULT))
                        } else {
                            consumer.consume(ConsumerData.Data(tracksResource.data))
                        }
                    }
                }
            }
        }
    }

    companion object{
        const val NO_RESULT = "No results"
    }
}