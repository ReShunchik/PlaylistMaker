package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.api.SharingInteractor
import com.example.playlistmaker.domain.settings.api.SharingRepository

class SharingInteractorImpl(
    private val sharingRepository: SharingRepository
): SharingInteractor {

    override fun shareApp() {
       sharingRepository.shareApp()
    }

    override fun openTerms() {
        sharingRepository.openTerms()
    }

    override fun openSupport() {
        sharingRepository.openSupport()
    }
}