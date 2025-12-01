package com.example.playlistmaker.domain.playlist.api

import android.net.Uri
import java.io.File

interface ImageRepository {

    suspend fun saveImage(fileName: String, uri: Uri)

    fun getImage(fileName: String): File?
}