package com.example.playlistmaker.domain.playlist.impl

import com.example.playlistmaker.domain.playlist.api.ImageInteractor
import com.example.playlistmaker.domain.playlist.api.ImageRepository
import java.io.File

class ImageInteractorImpl(
    private val imageRepository: ImageRepository
): ImageInteractor {

    override fun getImage(name: String): File? {
        return imageRepository.getImage(name)
    }

}