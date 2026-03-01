package com.example.playlistmaker.ui.editPlaylist.fragment

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.ui.editPlaylist.viewModel.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment: Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<EditPlaylistViewModel>()

    private lateinit var playlist: Playlist
    private var image: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener{
            findNavController().navigateUp()
        }

        setInfo()

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(requireContext())
                        .load(uri)
                        .transform(CenterCrop(), RoundedCorners(dpToPx(8f)))
                        .into(binding.addPlaylistPhoto)
                    image = uri
                }
            }

        binding.addPlaylistPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        val nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrEmpty()){
                    binding.createPlaylist.isEnabled = false
                } else {
                    binding.createPlaylist.isEnabled = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }
        binding.playlistName.addTextChangedListener(nameTextWatcher)

        binding.createPlaylist.setOnClickListener{
            val editedPlaylist = Playlist(
                playlist.id,
                binding.playlistName.text.toString(),
                binding.playlistDescription.text.toString(),
                playlist.tracks,
                playlist.imageName
            )
            viewModel.updatePlaylist(editedPlaylist, image)
            findNavController().navigateUp()
        }
    }

    private fun setInfo(){
        binding.fragmentTitle.text = requireContext().getString(R.string.edit_playlist)
        binding.createPlaylist.text = requireContext().getString(R.string.save)
        playlist = requireArguments().get(PLAYLIST_EDIT) as Playlist
        image = requireArguments().get(URI) as Uri?
        binding.playlistName.setText(playlist.name)
        if(playlist.descriotion.isNotEmpty()){
            binding.playlistDescription.setText(playlist.descriotion)
        }
        Glide.with(requireContext())
            .load(image)
            .placeholder(R.drawable.track_placeholder_100)
            .transform(CenterCrop(), RoundedCorners(dpToPx(8f)))
            .into(binding.addPlaylistPhoto)
    }

    fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            requireContext().resources.displayMetrics).toInt()
    }

    companion object{
        private const val PLAYLIST_EDIT = "playlist_edit"
        private const val URI = "uri"

        fun createArgs(playlist: Playlist, uri: Uri?): Bundle =
            bundleOf(PLAYLIST_EDIT to playlist, URI to uri)
    }

}