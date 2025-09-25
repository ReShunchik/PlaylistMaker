package com.example.playlistmaker.ui.mediaLibrary.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaLibraryBinding

class PlaylistFragment: Fragment() {

    private lateinit var binding: FragmentMediaLibraryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMediaLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addPlaylist.isVisible = true
        binding.info.text = requireContext().getString(R.string.no_playlist)
    }
}