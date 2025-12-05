package com.example.playlistmaker.ui.search.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.audioPlayer.fragment.AudioPlayerFragment
import com.example.playlistmaker.ui.search.adapters.TrackAdapter
import com.example.playlistmaker.ui.search.viewModel.SearchViewModel
import com.example.playlistmaker.ui.search.viewModel.TracksState
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment: Fragment() {


    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()

    private var searchText = ""
    private lateinit var trackAdapter: TrackAdapter

    private lateinit var searchTextWatcher: TextWatcher


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        binding.trackList.layoutManager = LinearLayoutManager(requireContext())
        binding.trackList.adapter = trackAdapter


        binding.clearButton.setOnClickListener{
            binding.searchInput.setText("")
            viewModel.searchDebounce("")
            trackAdapter.clearTracks()
        }

        binding.clearHistory.setOnClickListener{
            viewModel.clearHistory()
            trackAdapter.clearTracks()
            binding.searchHistory.isVisible = false
        }

        searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearButton.isVisible = clearButtonVisibility(s)
                viewModel?.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = s.toString()
            }
        }

        binding.searchInput.addTextChangedListener(searchTextWatcher)

        binding.updateRequest.setOnClickListener{
            viewModel?.searchRequest()
        }

        viewModel.observeTrackState().observe(viewLifecycleOwner){
            when (it) {
                is TracksState.Content -> showContent(it.tracks)
                is TracksState.Error -> showError()
                is TracksState.Empty -> {
                    if(it.history == null){
                        showNoResults()
                    } else {
                        showSearchHistory(it.history)
                    }
                }
                is TracksState.Loading -> showLoading()
            }
        }
    }

    private fun init(){
        val onTrackClickDebounce = debounce<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
            track ->
            viewModel.freshHistory(track)
            findNavController().navigate(
                R.id.action_searchFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(track)
            )
        }
        trackAdapter = TrackAdapter{
            track ->
            onTrackClickDebounce(track)
        }
        initHistory()
    }

    private fun showSearchHistory(history: ArrayList<Track>){
        trackAdapter.updateTracks(history)
        if(trackAdapter.itemCount == 0){
            binding.searchHistory.isVisible = false
        } else {
            renderSearchHistory()
        }
    }

    private fun initHistory(){
        binding.historyTrackList.layoutManager = LinearLayoutManager(requireContext())
        binding.historyTrackList.adapter = trackAdapter
        binding.clearHistory.setOnClickListener{
            trackAdapter.clearTracks()
            binding.searchHistory.isVisible = false
        }
    }


    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
        showSearchHistory(viewModel.getHistory())
    }

    private fun showLoading(){
        binding.trackList.isVisible = false
        binding.noSearchResult.isVisible = false
        binding.connectionError.isVisible = false
        binding.searchHistory.isVisible = false
        binding.progressBar.isVisible = true
    }

    private fun showContent(tracks: ArrayList<Track>){
        trackAdapter.updateTracks(tracks)
        binding.trackList.isVisible = true
        binding.noSearchResult.isVisible = false
        binding.connectionError.isVisible = false
        binding.searchHistory.isVisible = false
        binding.progressBar.isVisible = false
    }

    private fun showError(){
        binding.trackList.isVisible = false
        binding.noSearchResult.isVisible = false
        binding.connectionError.isVisible = true
        binding.searchHistory.isVisible = false
        binding.progressBar.isVisible = false
    }

    private fun showNoResults(){
        binding.trackList.isVisible = false
        binding.noSearchResult.isVisible = true
        binding.connectionError.isVisible = false
        binding.searchHistory.isVisible = false
        binding.progressBar.isVisible = false
    }

    private fun renderSearchHistory(){
        binding.trackList.isVisible = false
        binding.noSearchResult.isVisible = false
        binding.connectionError.isVisible = false
        binding.searchHistory.isVisible = true
        binding.progressBar.isVisible = false
    }


    override fun onDestroyView() {
        super.onDestroyView()
        searchTextWatcher?.let { binding.searchInput.removeTextChangedListener(it) }
        _binding = null
    }

    companion object{
        const val CLICK_DEBOUNCE_DELAY = 300L
    }
}