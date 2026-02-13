package com.example.playlistmaker.ui.search.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.search.viewModel.SearchViewModel
import com.example.playlistmaker.ui.themes.PlaylistMakerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import coil.compose.AsyncImage
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.audioPlayer.fragment.AudioPlayerFragment
import com.example.playlistmaker.ui.search.viewModel.TracksState
import com.example.playlistmaker.ui.themes.Blue

class SearchFragment: Fragment() {


    //private var _binding: FragmentSearchBinding? = null
    //private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()

    private var searchText = ""
    //private lateinit var trackAdapter: TrackAdapter

    //private lateinit var searchTextWatcher: TextWatcher


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return ComposeView(context = requireContext()).apply {
            setContent {
                PlaylistMakerTheme {
                    SearchFragmentUI(viewModel = viewModel, findNavController())
                }
            }
        }
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
    }*/

    /*private fun init(){
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
*/

    /*override fun onDestroyView() {
        super.onDestroyView()
        searchTextWatcher?.let { binding.searchInput.removeTextChangedListener(it) }
        _binding = null
    }*/

    companion object{
        const val CLICK_DEBOUNCE_DELAY = 300L
    }
}

@Composable
fun SearchFragmentUI(
    viewModel: SearchViewModel,
    navController: NavController
){
    val trackState: TracksState by viewModel.trackState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ){
        Text(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(16.dp),
            text = stringResource( R.string.search ),
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            color = MaterialTheme.colorScheme.primary
        )
        SearchField(viewModel)
        when(val state = trackState){
            is TracksState.Default -> { }
            is TracksState.Loading -> { ProgressBar() }
            is TracksState.Empty -> { NoSearchResults() }
            is TracksState.Error -> { ConnectionError(viewModel) }
            is TracksState.History -> { TrackHistory(viewModel, navController) }
            is TracksState.Content -> {
                Spacer(Modifier.height(16.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(state.tracks.size) {
                        TrackItem(
                            state.tracks.get(it),
                            viewModel,
                            navController)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchField(viewModel: SearchViewModel) {
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .padding(horizontal = 16.dp)
            .background(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                focusRequester.requestFocus()
                       },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_search_20),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier
                .padding(start = 12.dp)
                .size(20.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = 8.dp, end = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = text,
                onValueChange = {
                    text = it
                    viewModel.searchDebounce(it)
                                },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    lineHeight = 20.sp // Фиксированная высота строки
                ),
                singleLine = true,
                cursorBrush = SolidColor(Color.Black),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        innerTextField()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused && text.isEmpty()) {
                            viewModel.clearSearch()
                        } else if (text.isEmpty()){
                            viewModel.setDefault()
                        }
                    }
            )

            if (text.isEmpty()) {
                Text(
                    text = stringResource(R.string.search),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .fillMaxWidth()
                )
            }
        }

        if (text.isNotEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.ic_clear_40),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier
                    .clickable {
                        text = ""
                        viewModel.clearSearch()
                    }
                    .padding(end = 12.dp)
            )
        }
    }
}

@Composable
fun NoSearchResults(){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(100.dp))
        Image(
            painter = painterResource(R.drawable.ic_no_results_120),
            contentDescription = null
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.no_search_results),
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ConnectionError(
    viewModel: SearchViewModel
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(100.dp))
        Image(
            painter = painterResource(R.drawable.connection_error),
            contentDescription = null
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.connection_error),
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.error_info),
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                viewModel.searchRequest()
            },
            shape = RoundedCornerShape(54.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colorScheme.primary),
            content = {
                Text(
                    text = stringResource(R.string.update),
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }
        )
    }
}

@Composable
fun TrackItem(
    track: Track,
    viewModel: SearchViewModel,
    navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp)
            .clickable {
                viewModel.freshHistory(track)

                navController.navigate(
                    R.id.action_searchFragment_to_audioPlayerFragment,
                    AudioPlayerFragment.createArgs(track)
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.padding(vertical = 8.dp)){
            AsyncImage(
                model = track.artworkUrl100,
                contentDescription = null,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(2.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.track_placeholder_100)
            )
        }

        Column(Modifier
            .padding(start = 8.dp)
            .weight(1f)) {
            Text(
                text = track.trackName ,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
            )

            Row() {
                Text(
                    text = track.artistName,
                    fontSize = 11.sp,
                    fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    modifier = Modifier.widthIn(max = 80.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_point_12,),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                    modifier = Modifier
                        .size(12.dp)
                        .align(Alignment.CenterVertically)
                )

                Text(
                    text = track.trackTime,
                    fontSize = 11.sp,
                    fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.ic_move_on_24),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 20.dp)
        )
    }
}

@Composable
fun ProgressBar(){
    Box(
        modifier = Modifier.fillMaxWidth(),

    ){
        CircularProgressIndicator(
            modifier = Modifier
                .size(44.dp)
                .padding(top = 140.dp)
                .align(Alignment.Center),
            color = Blue,
        )
    }
}

@Composable
fun TrackHistory(
    viewModel: SearchViewModel,
    navController: NavController
){
    val tracks by viewModel.trackHistory.collectAsState()

    if (tracks.isNotEmpty()){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.history),
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(20.dp))
            LazyColumn(
                Modifier
                    .height(0.dp)
                    .weight(1f)
            ){
                items(tracks.size) {
                    TrackItem(
                        tracks.get(it),
                        viewModel,
                        navController)
                }
            }
            Column(Modifier.padding(bottom = 12.dp)) {
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = {
                        viewModel.clearHistory()
                        tracks.clear()
                    },
                    shape = RoundedCornerShape(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colorScheme.primary),
                    content = {
                        Text(
                            text = stringResource(R.string.clear_history),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )
                    }
                )
            }
        }
    }
}