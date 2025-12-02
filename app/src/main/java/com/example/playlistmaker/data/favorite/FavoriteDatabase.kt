package com.example.playlistmaker.data.favorite

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.favorite.dao.TrackDao
import com.example.playlistmaker.data.favorite.entity.TrackEntity
import com.example.playlistmaker.data.playlist.dao.PlaylistDao
import com.example.playlistmaker.data.playlist.dao.TrackPlaylistDao
import com.example.playlistmaker.data.playlist.entity.PlaylistEntity
import com.example.playlistmaker.data.playlist.entity.TrackPlaylistEntity

@Database(
    version = 1,
    entities = [
                TrackEntity::class,
                PlaylistEntity::class,
                TrackPlaylistEntity::class],
    exportSchema = false)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun trackPlaylistDao(): TrackPlaylistDao

}