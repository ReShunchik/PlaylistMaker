package com.example.playlistmaker.di

import com.example.playlistmaker.ui.mediaLibrary.adapters.PlaylistAdapter
import org.koin.dsl.module


val adapterModule = module {

    factory{
        PlaylistAdapter(get())
    }
}