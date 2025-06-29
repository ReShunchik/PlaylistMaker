package com.example.playlistmaker.Activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Utils.App
import com.example.playlistmaker.R
import com.example.playlistmaker.adapters.TrackAdapter
import com.example.playlistmaker.datas.Track
import com.example.playlistmaker.datas.TracksResponse
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class SearchActivity : AppCompatActivity() {

    private var searchText = ""
    private lateinit var trackList: RecyclerView
    private lateinit var trackHistoryList: RecyclerView
    private lateinit var noSearchLayout: LinearLayout
    private lateinit var connectionErrorLayout: LinearLayout
    private lateinit var searchHistory: LinearLayout
    private lateinit var trackAdapter: TrackAdapter

    private val retrofit: Retrofit

    private val iTunesService: ITunesAPI

    init {
        retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        iTunesService = retrofit.create(ITunesAPI::class.java)
    }

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
                }
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = s.toString()
            }
        }

        searchField.addTextChangedListener(searchTextWatcher)

        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                findTracks(searchField.text.toString())
                true
            } else {
                false
            }
        }

        searchField.setOnFocusChangeListener{view, hasFocus ->
            if(hasFocus && searchField.text.isEmpty()){
                showSearchHistory()
            }
        }

        updateButton.setOnClickListener{
            findTracks(searchField.text.toString())
        }
    }

    private fun initViews(){
        trackList = findViewById(R.id.track_list)
        noSearchLayout = findViewById(R.id.no_search_result)
        connectionErrorLayout = findViewById(R.id.connection_error)
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

    private fun findTracks(searchText: String){
        trackList.visibility = View.GONE
        allLayoutGone()
        iTunesService.search(searchText)
            .enqueue(object : Callback<TracksResponse>{
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    if(response.isSuccessful){
                        val results = response.body()?.results as List<Track>
                        if (!results.isNullOrEmpty()) {
                            trackList.visibility = View.VISIBLE
                            trackAdapter.updateTracks(results)
                        } else{
                            noSearchLayout.visibility = View.VISIBLE
                        }
                    }
                }
                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    connectionErrorLayout.visibility = View.VISIBLE
                }
            })
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

    interface ITunesAPI {
        @GET("search")
        fun search(
            @Query("term") term: String,
            @Query("entity") entity: String = "musicTrack",
            @Query("limit") limit: Int = 25
        ): Call<TracksResponse>
    }

    companion object{
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_DEF = ""
    }
}