package com.example.playlistmaker.data.playlist.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.favorite.entity.TrackEntity
import com.example.playlistmaker.data.playlist.entity.TrackPlaylistEntity

@Dao
interface TrackPlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackPlaylistEntity)

    @Query("SELECT * FROM track_playlist_table WHERE id = :trackId")
    suspend fun getTrackById(trackId: Long): TrackPlaylistEntity
}