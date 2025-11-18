package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.favorite.entity.TrackEntity
import com.example.playlistmaker.domain.search.models.Track

class TrackDbConverter {

    fun map(track: Track): TrackEntity{
        return(TrackEntity(
            track.id,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.country,
            track.genre,
            track.album,
            track.year,
            track.previewUrl
        ))
    }

    fun map(track: TrackEntity): Track{
        return(Track(
            track.id,
            track.trackName,
            track.artistName,
            track.trackTime.toString(),
            track.artworkUrl100,
            track.country,
            track.genre,
            track.album,
            track.year,
            track.previewUrl
        ))
    }
}