package com.example.playlistmaker.ui.mediaLibrary.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaLibraryContentBinding
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.ui.mediaLibrary.adapters.PlaylistAdapter
import com.example.playlistmaker.ui.mediaLibrary.viewModel.PlaylistState
import com.example.playlistmaker.ui.mediaLibrary.viewModel.PlaylistViewModel
import com.example.playlistmaker.ui.playList.fragment.TrackPlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

class PlaylistFragment: Fragment(), KoinComponent {

    private var _binding: FragmentMediaLibraryContentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlaylistViewModel>()

    private lateinit var adapter: PlaylistAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentMediaLibraryContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addPlaylist.isVisible = true
        binding.info.text = requireContext().getString(R.string.no_playlist)

        setAdapter()
        binding.trackList.adapter = adapter
        binding.trackList.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.observePlaylistState().observe(viewLifecycleOwner){
            when(it){
                is PlaylistState.Empty -> showEmptyScreen()
                is PlaylistState.Content -> showPlaylists(it.playlists)
            }
        }

        viewModel.fillData()

        binding.addPlaylist.setOnClickListener{
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_createPlaylistFragment)
        }
    }

    private fun setAdapter(){
        val onItemClick: (playlistId: Long) -> Unit = {
            playlistId ->
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_trackPlaylistFragment,
                TrackPlaylistFragment.createArgs(playlistId)
            )
        }
        adapter = PlaylistAdapter(onItemClick)
    }

    private fun showEmptyScreen(){
        binding.trackList.isVisible = false
        binding.messageStub.isVisible = true
        binding.info.text = requireContext().getString(R.string.no_playlist)
    }

    private fun showPlaylists(playlist: List<Playlist>){
        binding.trackList.isVisible = true
        binding.messageStub.isVisible = false
        adapter.updatePlaylists(playlist)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}