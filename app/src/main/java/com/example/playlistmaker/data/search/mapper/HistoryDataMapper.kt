package com.example.playlistmaker.data.search.mapper

import com.example.playlistmaker.data.dto.SavedTrackDto
import com.example.playlistmaker.domain.search.models.Track

object HistoryDataMapper {

    fun map(results: List<SavedTrackDto>): ArrayList<Track> {
        return results.map {
            Track(
                it.id,
                it.trackName,
                it.artistName,
                it.trackTime,
                it.artworkUrl100,
                it.country,
                it.genre,
                it.album,
                it.year,
                it.previewUrl
            )
        } as ArrayList<Track>
    }
}