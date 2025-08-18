package com.example.playlistmaker.data.repository

import android.content.SharedPreferences
import com.example.playlistmaker.data.dto.SavedTrackDto
import com.example.playlistmaker.domain.api.TracksHistoryRepository
import com.example.playlistmaker.domain.models.Track

import com.google.gson.Gson

class TracksHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences
): TracksHistoryRepository{

    override fun freshHistory(track: SavedTrackDto) {
        val tracks = ArrayList<SavedTrackDto>()
        tracks.addAll(readTracks())
        if(tracks.contains(track)){
            tracks.remove(track)
        }
        tracks.add(0, track)
        if(tracks.lastIndex > 9){
            tracks.removeAt(tracks.lastIndex)
        }
        writeTracks(tracks)
    }

    override fun clearHistory() {
        sharedPreferences
            .edit()
            .remove(HISTORY)
            .apply()
    }

    override fun readTracks(): Array<SavedTrackDto> {
        val json = sharedPreferences.getString(HISTORY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<SavedTrackDto>::class.java)
    }

    override fun getTracks(): Array<Track> {
        val json = sharedPreferences.getString(HISTORY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    override fun writeTracks(tracks: ArrayList<SavedTrackDto>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(HISTORY, json)
            .apply()
    }

    companion object{
        const val HISTORY = "history"
    }

}