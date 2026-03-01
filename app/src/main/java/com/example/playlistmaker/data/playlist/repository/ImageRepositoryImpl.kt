package com.example.playlistmaker.data.playlist.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.example.playlistmaker.domain.playlist.api.ImageRepository
import java.io.File
import java.io.FileOutputStream

class ImageRepositoryImpl(
    private val context: Context,
): ImageRepository {

    override suspend fun saveImage(name: String, uri: Uri) {
        Log.d("Image", "Image start saving")
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), CATALOG)
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        val fileName = name + EXTENSION
        val file = File(filePath, fileName)
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        Log.d("Image", "Image saved")
    }


    override fun getImage(name: String): File? {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), CATALOG)
        val file = File(filePath, name + EXTENSION)
        return if (file.exists()) file else null
    }

    companion object{
        const val CATALOG = "playlistalbum"
        const val EXTENSION = ".jpg"
    }
}