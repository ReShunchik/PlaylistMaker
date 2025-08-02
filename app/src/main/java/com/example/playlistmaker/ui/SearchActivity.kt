package com.example.playlistmaker.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.consumer.Consumer
import com.example.playlistmaker.domain.api.consumer.ConsumerData
import com.example.playlistmaker.ui.adapters.TrackAdapter
import com.example.playlistmaker.domain.models.Track
import com.google.android.material.button.MaterialButton


class SearchActivity : AppCompatActivity() {

    private var searchText = ""
    private lateinit var trackList: RecyclerView
    private lateinit var trackHistoryList: RecyclerView
    private lateinit var noSearchLayout: LinearLayout
    private lateinit var connectionErrorLayout: LinearLayout
    private lateinit var searchHistory: LinearLayout
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var progressBar: ProgressBar

    private val getTracksInteractor = Creator.provideTracksInteractor()

    private var searchRunnable = Runnable { searchTracks() }
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initViews()
        val searchField = findViewById<EditText>(R.id.search_input)
        val clearButton = findViewById<ImageButton>(R.id.clear_button)
        val updateButton = findViewById<MaterialButton>(R.id.update_request)
        searchHistory = findViewById(R.id.search_history)
        trackList.layoutManager = LinearLayoutManager(this)
        trackList.adapter = trackAdapter

        if(savedInstanceState != null){
            searchText = savedInstanceState.getString(SEARCH_TEXT, SEARCH_DEF)
            searchField.setText(searchText)
        }

        clearButton.setOnClickListener{
            searchField.setText("")
            trackAdapter.clearTracks()
            showSearchHistory()
        }

        findViewById<ImageView>(R.id.button_back).setOnClickListener{
            finish()
        }

        val searchTextWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                if (searchField.hasFocus() && s?.isEmpty() == true) {
                    showSearchHistory()
                } else {
                    searchHistory.visibility = View.GONE
                    searchDebounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = s.toString()
            }
        }

        searchField.addTextChangedListener(searchTextWatcher)

        searchField.setOnFocusChangeListener{view, hasFocus ->
            if(hasFocus && searchField.text.isEmpty()){
                showSearchHistory()
            }
        }

        updateButton.setOnClickListener{
            searchTracks()
        }
    }

    private fun initViews(){
        trackList = findViewById(R.id.track_list)
        noSearchLayout = findViewById(R.id.no_search_result)
        connectionErrorLayout = findViewById(R.id.connection_error)
        progressBar = findViewById(R.id.progress_bar)
        trackAdapter = TrackAdapter(this, applicationContext as App)
        initHistory()
    }

    private fun showSearchHistory(){
        trackAdapter.updateHistoryTracks()
        if(trackAdapter.itemCount == 0){
            searchHistory.visibility = View.GONE
        } else {
            allLayoutGone()
            searchHistory.visibility = View.VISIBLE
        }
    }

    private fun initHistory(){
        trackHistoryList = findViewById(R.id.history_track_list)
        trackHistoryList.layoutManager = LinearLayoutManager(this)
        trackHistoryList.adapter = trackAdapter
        val clearHistory = findViewById<MaterialButton>(R.id.clear_history)
        clearHistory.setOnClickListener{
            trackAdapter.clearHistory()
            searchHistory.visibility = View.GONE
        }
    }

    private fun searchTracks(){
        trackList.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        allLayoutGone()

        getTracksInteractor.searchTracks(
            searchText,
            consumer = object : Consumer<List<Track>> {

                override fun consume(data: ConsumerData<List<Track>>) {
                    when(data){
                        is ConsumerData.Error -> {
                            when(data.message){
                                CONNECTION_ERROR -> {
                                    runOnUiThread{
                                        connectionErrorLayout.visibility = View.VISIBLE
                                        progressBar.visibility = View.GONE
                                    }
                                }
                                NO_RESULTS -> {
                                    runOnUiThread{
                                        noSearchLayout.visibility = View.VISIBLE
                                        progressBar.visibility = View.GONE
                                    }
                                }
                            }
                        }

                        is ConsumerData.Data -> {
                            runOnUiThread{
                                trackList.visibility = View.VISIBLE
                                trackAdapter.updateTracks(data.value)
                                progressBar.visibility = View.GONE
                            }
                        }
                    }

                }
            }
        )
    }

    private fun searchDebounce(){
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun allLayoutGone(){
        trackList.visibility = View.GONE
        noSearchLayout.visibility = View.GONE
        connectionErrorLayout.visibility = View.GONE
        searchHistory.visibility = View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }



    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
    }

    companion object{
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val SEARCH_DEF = ""
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CONNECTION_ERROR = "Connection Error"
        private const val NO_RESULTS = "No results"
    }
}