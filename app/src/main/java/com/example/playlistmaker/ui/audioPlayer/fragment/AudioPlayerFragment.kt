package com.example.playlistmaker.ui.audioPlayer.fragment

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.services.MusicService
import com.example.playlistmaker.ui.audioPlayer.adapters.BottomSheetAdapter
import com.example.playlistmaker.ui.audioPlayer.viewModel.AudioPlayerViewModel
import com.example.playlistmaker.ui.audioPlayer.viewModel.PlayerState
import com.example.playlistmaker.ui.audioPlayer.viewModel.TrackState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.component.KoinComponent
import java.time.OffsetDateTime

class AudioPlayerFragment : Fragment(), KoinComponent {

    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AudioPlayerViewModel
    private var songUrl: String = ""
    private var contentText: String = ""

    private val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicServiceBinder
            viewModel.setAudioPlayerControl(binder.getService())
        }

        override fun onServiceDisconnected(ame: ComponentName?) {
            viewModel.removeAudioPlayerControl()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean->
        if (isGranted) {
            bindMusicService()
        }
    }

    private lateinit var track: Track

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInfo()

        binding.buttonBack.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.favoriteButton.setOnClickListener{
            if(track != null){
                val track = track
                when(viewModel.getCurrentTrackState()){
                    is TrackState.IsFavorite -> viewModel.deleteTrackFromFavorite(track)
                    is TrackState.NotFavorite -> viewModel.addToFavorite(track)
                }
            }
        }

        viewModel.observeTrackStateLiveData().observe(viewLifecycleOwner){
            when(it){
                is TrackState.IsFavorite -> binding.favoriteButton.setImageResource(R.drawable.ic_like_24)
                is TrackState.NotFavorite -> binding.favoriteButton.setImageResource(R.drawable.ic_not_like_24)
            }
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.addButton.setOnClickListener{
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.darkned.isVisible = true
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.darkned.isVisible = false
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        viewModel.fillData()
        viewModel.observePlaylistsLiveData().observe(viewLifecycleOwner){
            val onItemClick: (message: String) -> Unit = { message ->
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                val toast = Toast(requireContext())

                val layout = layoutInflater.inflate(
                    R.layout.playlist_toast,
                    null
                )
                val textView = layout.findViewById<TextView>(R.id.info)
                textView.setText(message)

                toast.duration = Toast.LENGTH_LONG
                toast.view = layout
                toast.show()
                viewModel.fillData()
            }
            val adapter = BottomSheetAdapter(track,onItemClick, viewModel.onItemClickDb, viewModel.getPlaylistImage)
            adapter.setPlaylists(it)
            binding.playlists.adapter = adapter
            binding.playlists.layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.observePlayerStateLiveData().observe(viewLifecycleOwner){
            renderTrackTime(it)
        }

        binding.addPlaylist.setOnClickListener{
            findNavController().navigate(
                R.id.action_audioPlayerFragment_to_createPlaylistFragment
            )
        }

        binding.playerButton.setOnClickListener{
            viewModel.onPlayerButtonClicked()
        }
    }


    private fun setInfo(){
        track = requireArguments().get(TRACK) as Track
        if (track != null){
            contentText = track.artistName + " - " + track.trackName
            val atworkUrl512 = track?.artworkUrl100?.replace("100x100", "512x512")
            Glide.with(this)
                .load(atworkUrl512)
                .placeholder(R.drawable.track_placeholder_512)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(8f)))
                .into(binding.atwork)

            binding.trackName.text = track?.trackName
            binding.trackArtist.text = track?.artistName
            binding.durationInfo.text = track?.trackTime
            binding.genreInfo.text = track?.genre
            binding.countryInfo.text = track?.country
            if(track?.year != null){
                binding.yearGroup.visibility = View.VISIBLE
                binding.yearInfo.text = OffsetDateTime.parse(track?.year).year.toString()
            }
            if(track?.album != null){
                binding.albumGroup.visibility = View.VISIBLE
                binding.albumInfo.text = track?.album
            }
            viewModel = getViewModel()
            viewModel.checkIsFavoriteTrack(track.trackId)
            songUrl = track.previewUrl
        } else {
            findNavController().navigateUp()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            bindMusicService()
        }
    }

    fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            this.resources.displayMetrics).toInt()
    }

    private fun renderTrackTime(state: PlayerState){
        binding.currentTime.text = state.progress
        if(state is PlayerState.Default){
            binding.playerButton.enableButton(false)
        }
        else if (state is PlayerState.Prepared){
            binding.playerButton.isTrackPlaying(false)
            binding.playerButton.enableButton(true)
        }
        else {
            binding.playerButton.enableButton(true)
        }
    }

    private fun bindMusicService(){
        val intent = Intent(requireContext(), MusicService::class.java).apply{
            putExtra(SONG_URL, songUrl)
            putExtra(CONTENT_TEXT, contentText)
        }

        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unBindMusicService(){
        requireContext().unbindService(serviceConnection)
    }

    override fun onStart(){
        super.onStart()
        viewModel.hideNotification()
    }

    override fun onStop() {
        super.onStop()
        viewModel.showNotification()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unBindMusicService()
        _binding = null
    }

    companion object{
        private const val TRACK = "track"
        private const val SONG_URL = "song_url"
        private const val CONTENT_TEXT = "content_text"

        fun createArgs(track: Track): Bundle =
            bundleOf(TRACK to track)
    }
}