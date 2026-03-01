package com.example.playlistmaker.ui.mediaLibrary.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaLibraryContentBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.audioPlayer.fragment.AudioPlayerFragment
import com.example.playlistmaker.ui.mediaLibrary.viewModel.FavoriteState
import com.example.playlistmaker.ui.mediaLibrary.viewModel.FavoriteViewModel
import com.example.playlistmaker.ui.search.adapters.TrackAdapter
import com.example.playlistmaker.utils.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteFragment: Fragment() {

    private var _binding: FragmentMediaLibraryContentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FavoriteViewModel>()

    private lateinit var trackAdapter: TrackAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentMediaLibraryContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        binding.addPlaylist.isVisible = false

        binding.trackList.layoutManager = LinearLayoutManager(requireContext())
        binding.trackList.adapter = trackAdapter

        viewModel.fillData()
        viewModel.observeFavoriteState().observe(viewLifecycleOwner){
            render(it)
        }
    }

    private fun init(){
        val onTrackClickDebounce = debounce<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
                track ->
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(track)
            )
        }
        trackAdapter = TrackAdapter{
                track ->
            onTrackClickDebounce(track)
        }
    }

    private fun render(state: FavoriteState){
        when(state){
            is FavoriteState.Content -> showFavorite(state.tracks)
            is FavoriteState.Empty -> showEmpty(state.message)
        }
    }

    private fun showFavorite(tracks: List<Track>){
        binding.messageStub.isVisible = false
        binding.trackList.isVisible = true
        trackAdapter.updateTracks(tracks)
    }

    private fun showEmpty(message: String){
        binding.messageStub.isVisible = true
        binding.trackList.isVisible = false
        binding.info.text = message
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        const val CLICK_DEBOUNCE_DELAY = 300L
    }
}