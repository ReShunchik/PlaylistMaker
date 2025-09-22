package com.example.playlistmaker.ui.search.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.ui.search.adapters.TrackAdapter
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.audioPlayer.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.search.viewModel.SearchViewModel
import com.example.playlistmaker.ui.search.viewModel.TracksState
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel by viewModel<SearchViewModel>()

    private var searchText = ""
    private lateinit var trackAdapter: TrackAdapter

    private lateinit var searchTextWatcher: TextWatcher


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        binding.trackList.layoutManager = LinearLayoutManager(this)
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

        binding.buttonBack.setOnClickListener{
            finish()
        }

        searchTextWatcher = object : TextWatcher{
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

        viewModel.observeTrackState().observe(this){
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
        trackAdapter = TrackAdapter{ track ->
            viewModel.freshHistory(track)
            val intent = Intent(this, AudioPlayerActivity::class.java)
            intent.putExtra(TRACK, track)
            startActivity(intent)
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
        binding.historyTrackList.layoutManager = LinearLayoutManager(this)
        binding.historyTrackList.adapter = trackAdapter
        binding.clearHistory.setOnClickListener{
            trackAdapter.clearTracks()
            binding.searchHistory.isVisible = false
        }
    }


    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
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


    override fun onDestroy() {
        super.onDestroy()
        searchTextWatcher?.let { binding.searchInput.removeTextChangedListener(it) }
    }

    companion object{
        const val TRACK = "track"
    }
}