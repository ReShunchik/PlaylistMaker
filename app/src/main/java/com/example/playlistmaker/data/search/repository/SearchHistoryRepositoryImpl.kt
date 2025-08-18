package com.example.playlistmaker.data.search.repository


import com.example.playlistmaker.data.dto.SavedTrackDto
import com.example.playlistmaker.data.search.mapper.HistoryDataMapper
import com.example.playlistmaker.data.search.storage.StorageClient
import com.example.playlistmaker.domain.search.api.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track

class SearchHistoryRepositoryImpl(
    private val storageClient: StorageClient<ArrayList<SavedTrackDto>>
): SearchHistoryRepository {

    override fun freshHistory(track: SavedTrackDto) {
        val tracks = ArrayList<SavedTrackDto>()
        tracks.addAll(readHistory())
        if(tracks.contains(track)){
            tracks.remove(track)
        }
        tracks.add(0, track)
        if(tracks.lastIndex > 9){
            tracks.removeAt(tracks.lastIndex)
        }
        saveToHistory(tracks)
    }

    override fun clearHistory() {
        storageClient.clearData()
    }

    fun readHistory(): ArrayList<SavedTrackDto> {
        val history = storageClient.getData() ?: ArrayList()
        return (history)
    }

    override fun getHistory(): ArrayList<Track> {
        val history = storageClient.getData() ?: ArrayList()
        return HistoryDataMapper.map(history)
    }

    override fun saveToHistory(tracks: ArrayList<SavedTrackDto>) {
        storageClient.storeData(tracks)
    }

    companion object{
        const val HISTORY = "history"
    }

}