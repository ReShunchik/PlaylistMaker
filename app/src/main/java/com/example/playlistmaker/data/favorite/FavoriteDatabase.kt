package com.example.playlistmaker.data.favorite

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.favorite.dao.TrackDao
import com.example.playlistmaker.data.favorite.entity.TrackEntity

@Database(
    version = 1,
    entities = [TrackEntity::class],
    exportSchema = false)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

}