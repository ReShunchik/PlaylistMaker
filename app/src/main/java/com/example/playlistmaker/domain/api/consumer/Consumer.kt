package com.example.playlistmaker.domain.api.consumer


interface Consumer<T> {
    fun consume(data: ConsumerData<T>)
}