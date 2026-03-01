package com.example.playlistmaker.data.mapper

import android.util.Log
import com.example.playlistmaker.data.playlist.entity.PlaylistEntity
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConverter(
    private val gson: Gson
) {

    private val type = object : TypeToken<ArrayList<Long>>() {}.type

    fun map(playlist: Playlist): PlaylistEntity{
        Log.d("Image", "converted")
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.descriotion,
            gson.toJson(playlist.tracks, type),
            playlist.imageName
        )
    }

    fun map(playlist: PlaylistEntity): Playlist{
        Log.d("Image", "converted")
        return Playlist(
            playlist.id,
            playlist.name,
            playlist.descriotion,
            gson.fromJson(playlist.tracks, type),
            playlist.imageName
        )
    }
}