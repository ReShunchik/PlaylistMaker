package com.example.playlistmaker.domain.sharing.api

interface SharingRepository {
    fun shareApp()
    fun openTerms()
    fun openSupport()
    fun sharePlaylist(message: String)
}