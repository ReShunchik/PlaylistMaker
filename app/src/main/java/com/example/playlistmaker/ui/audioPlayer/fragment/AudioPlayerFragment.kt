package com.example.playlistmaker.ui.audioPlayer.fragment

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.audioPlayer.adapters.BottomSheetAdapter
import com.example.playlistmaker.ui.audioPlayer.viewModel.AudioPlayerViewModel
import com.example.playlistmaker.ui.audioPlayer.viewModel.TrackState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf
import java.time.OffsetDateTime

class AudioPlayerFragment : Fragment(), KoinComponent {

    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AudioPlayerViewModel

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

        binding.playerButton.setOnClickListener{
            viewModel.onPlayButtonClicked()
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

        viewModel.observePlayerState().observe(viewLifecycleOwner){
            binding.playerButton.isEnabled = it.isPlayButtonEnabled
            binding.currentTime.text = it.progress
            if(it.isPlaying){
                showPlaying()
            } else {
                showPaused()
            }
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.addButton.setOnClickListener{
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // newState — новое состояние BottomSheet
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.main.alpha = 0.5F
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.main.alpha = 1F
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

        binding.addPlaylist.setOnClickListener{
            findNavController().navigate(
                R.id.action_audioPlayerFragment_to_createPlaylistFragment
            )
        }
    }


    private fun setInfo(){
        track = requireArguments().get(TRACK) as Track
        if (track != null){
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
            viewModel = getViewModel { parametersOf(track?.previewUrl) }
            viewModel.checkIsFavoriteTrack(track.trackId)
        } else {
            findNavController().navigateUp()
        }
    }

    fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            this.resources.displayMetrics).toInt()
    }

    private fun showPlaying(){
        binding.playerButton.setImageResource(R.drawable.ic_pause_84)
    }

    private fun showPaused(){
        binding.playerButton.setImageResource(R.drawable.ic_play_84)
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        private const val TRACK = "track"

        fun createArgs(track: Track): Bundle =
            bundleOf(TRACK to track)
    }
}