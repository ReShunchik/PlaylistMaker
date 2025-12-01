package com.example.playlistmaker.domain.playlist.api

import java.io.File

interface ImageInteractor {

    fun getImage(name: String): File?
}