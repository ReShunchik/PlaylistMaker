package com.example.playlistmaker.domain.mapper

import com.example.playlistmaker.data.dto.SavedTrackDto
import com.example.playlistmaker.domain.search.models.Track

object DomainMapper {

    fun map(results: ArrayList<Track>): ArrayList<SavedTrackDto> {
        return ArrayList(results.map {
            SavedTrackDto(
                it.trackId,
                it.trackName,
                it.artistName,
                it.trackTime,
                it.artworkUrl100,
                it.country,
                it.genre,
                it.album,
                it.year,
                it.previewUrl)
        }
        )
    }

    fun map(track: Track): SavedTrackDto {
        return SavedTrackDto(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.country,
            track.genre,
            track.album,
            track.year,
            track.previewUrl
        )
    }
}