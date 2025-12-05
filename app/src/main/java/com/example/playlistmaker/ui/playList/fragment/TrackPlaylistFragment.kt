package com.example.playlistmaker.ui.playList.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.audioPlayer.fragment.AudioPlayerFragment
import com.example.playlistmaker.ui.editPlaylist.fragment.EditPlaylistFragment
import com.example.playlistmaker.ui.playList.adapters.TrackPlaylistAdapter
import com.example.playlistmaker.ui.playList.viewModel.TrackPlaylistState
import com.example.playlistmaker.ui.playList.viewModel.TrackPlaylistViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrackPlaylistFragment: Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<TrackPlaylistViewModel>()

    private lateinit var playlist: Playlist
    private var image: Uri? = null
    private lateinit var adapter: TrackPlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setImageSize()
        setInfo()
        setAdapter()

        viewModel.observeTracksState().observe(viewLifecycleOwner){
            setPlaylistTracks(it)
        }
        viewModel.observeImagePlaylist().observe(viewLifecycleOwner){
            image = it
            setImage(image, binding.playlistImage)
            setImage(image, binding.playlistMenuImage)
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        binding.buttonBack.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.buttonShare.setOnClickListener{
            shareTracks()
        }

        val menuBottomSheetBehavior = BottomSheetBehavior.from(binding.menuBottomSheet)
        menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.buttonMenu.setOnClickListener{
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.share.setOnClickListener{
            shareTracks()
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        menuBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
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

        binding.delete.setOnClickListener{
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.darkned.isVisible = true
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(requireContext().getString(R.string.dialog_title_delete_pl))
                .setMessage(requireContext().getString(R.string.dialog_message_delete_pl))
                .setPositiveButton(requireContext().getString(R.string.yes)) { dialog, which ->
                    viewModel.deletePlaylist(playlist)
                    binding.darkned.isVisible = false
                    findNavController().navigateUp()
                }
                .setNegativeButton(requireContext().getString(R.string.no)){ dialog, which ->
                    dialog.dismiss()
                    binding.darkned.isVisible = false
                }
                .show()

        }

        binding.edit.setOnClickListener{
            findNavController().navigate(
                R.id.action_trackPlaylistFragment_to_editPlaylistFragment,
                EditPlaylistFragment.createArgs(playlist, image)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        setInfo()
    }

    private fun showToast(){
        val toast = Toast(requireContext())

        val layout = layoutInflater.inflate(
            R.layout.playlist_toast,
            null
        )
        val textView = layout.findViewById<TextView>(R.id.info)
        val info =
            requireContext().getString(R.string.no_tracks)
        textView.setText(info)

        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }

    private fun setImage(uri: Uri?, view: ImageView){
        Glide.with(requireContext())
            .load(uri)
            .placeholder(R.drawable.track_placeholder_100)
            .centerCrop()
            .into(view)
    }

    private fun shareTracks(){
        if(playlist.tracks.isNotEmpty()){
            viewModel.sharePlaylist(playlist)
        } else {
            showToast()
        }
    }

    private fun setImageSize(){
        val screenWidth = requireActivity().windowManager.defaultDisplay.width
        binding.playlistImage.layoutParams = binding.playlistImage.layoutParams.apply {
            width = screenWidth
            height = screenWidth
        }

    }

    private fun setInfo(){
        playlist = requireArguments().get(PLAYLIST) as Playlist
        viewModel.fillData(playlist.tracks)
        viewModel.fillImage(playlist.imageName)
        binding.playlistName.text = playlist.name
        val description = playlist.descriotion
        if(description.isNullOrEmpty()){
            binding.playlistDescription.isVisible = false
        } else {
            binding.playlistDescription.isVisible = true
            binding.playlistDescription.text = playlist.descriotion
        }

        binding.menuPlaylistName.text = playlist.name
        binding.menuTrackCount.text = playlist.tracks.size.toString() + " треков"
    }

    private fun setPlaylistTracks(state: TrackPlaylistState){
        when(state){
            is TrackPlaylistState.Empty -> {
                binding.playlistTrackCount.text = "0 треков"
                binding.playlistTime.text = "0 минут"
                adapter.clearTracks()
            }
            is TrackPlaylistState.Content -> {
                val count = state.tracks.size
                val case1: String
                if(count == 1){
                    case1 = requireContext().getString(R.string.track1)
                } else if ((count >= 2) and (count <= 4)) {
                    case1 = requireContext().getString(R.string.track2)
                } else {
                    case1 = requireContext().getString(R.string.track3)
                }
                binding.playlistTrackCount.text = (state.tracks.size.toString() + " " + case1)
                val time = state.time.toInt()
                val case2: String
                if(time == 1){
                    case2 = requireContext().getString(R.string.minute1)
                } else if ((count >= 2) and (count <= 4)) {
                    case2 = requireContext().getString(R.string.minute2)
                } else {
                    case2 = requireContext().getString(R.string.minute3)
                }
                binding.playlistTime.text = (state.time + " " + case2)
                adapter.updateTracks(state.tracks)
            }
        }
    }

    private fun setAdapter(){
        val onItemClick:(track: Track) -> Unit = {
            track ->
            findNavController().navigate(
                R.id.action_trackPlaylistFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(track)
            )
        }
        val onItemLongClick: (track: Track) -> Unit = {
            track ->
            binding.darkned.isVisible = true
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(requireContext().getString(R.string.dialog_title_delete))
                .setMessage(requireContext().getString(R.string.dialog_message_delete))
                .setPositiveButton(requireContext().getString(R.string.yes)) { dialog, which ->
                    viewModel.deleteTrack(playlist, track.trackId)
                    binding.darkned.isVisible = false
                    dialog.dismiss()
                }
                .setNegativeButton(requireContext().getString(R.string.no)){ dialog, which ->
                    binding.darkned.isVisible = false
                    dialog.dismiss()
                }
                .show()
        }
        adapter = TrackPlaylistAdapter(onItemClick, onItemLongClick)
        binding.tracks.adapter = adapter
        binding.tracks.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object{
        private const val PLAYLIST = "playlist"

        fun createArgs(playlist: Playlist): Bundle =
            bundleOf(PLAYLIST to playlist)
    }
}