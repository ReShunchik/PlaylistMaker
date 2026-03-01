package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.favorite.entity.TrackEntity
import com.example.playlistmaker.data.playlist.entity.TrackPlaylistEntity
import com.example.playlistmaker.domain.search.models.Track

class TrackPlaylistDbConverter {

    fun map(track: Track): TrackPlaylistEntity {
        return TrackPlaylistEntity(
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

    fun map(track: TrackPlaylistEntity): Track {
        return Track(
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
        )
    }
}