package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.search.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

object DataMapper {

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    fun map(results: List<TrackDto>): List<Track> {
        return results.map {
            Track(it.trackName,
                it.artistName,
                getMmSS(it.trackTime),
                it.artworkUrl100,
                it.country,
                it.genre,
                it.album,
                it.year,
                it.previewUrl)
        }
    }

    private fun getMmSS(time: Long): String{
        return dateFormat.format(time)
    }

}