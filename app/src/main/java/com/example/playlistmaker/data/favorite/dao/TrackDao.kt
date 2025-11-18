package com.example.playlistmaker.data.favorite.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.favorite.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM favorite_table")
    suspend fun getFavoriteTracks(): List<TrackEntity>

    @Query("SELECT * FROM favorite_table WHERE id = :trackId")
    suspend fun getTrackById(trackId: Long): TrackEntity?
}