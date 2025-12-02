package com.example.playlistmaker.ui.createPlaylist.fragment

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.ui.createPlaylist.viewModel.CreatePlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment: Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!

    private var image: Uri? = null

    private val viewModel by viewModel<CreatePlaylistViewModel>()

    private lateinit var dialogAlert: AlertDialog

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

        binding.createPlaylist.isEnabled = false

        val confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireContext().getString(R.string.dialog_title))
            .setMessage(requireContext().getString(R.string.dialog_message))
            .setNeutralButton(requireContext().getString(R.string.cancel)) { dialog, which ->
                dialogAlert.dismiss()
            }
            .setPositiveButton(requireContext().getString(R.string.finish)) { dialog, which ->
                findNavController().navigateUp()
            }


        binding.buttonBack.setOnClickListener{
            val name = binding.playlistName.text.toString()
            val description = binding.playlistDescription.text.toString()
            if((image != null) or name.isNotEmpty() or description.isNotEmpty()){
                dialogAlert = confirmDialog.show()
            } else {
                findNavController().navigateUp()
            }

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

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(requireContext())
                        .load(uri)
                        .centerCrop()
                        .transform(RoundedCorners(dpToPx(8f)))
                        .into(binding.addPlaylistPhoto)
                    //binding.addPlaylistPhoto.setImageURI(uri)
                    image = uri
                }
            }

        binding.addPlaylistPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.createPlaylist.setOnClickListener{
            if(it.isEnabled == true){
                val name = binding.playlistName.text.toString()
                val description = binding.playlistDescription.text.toString()
                viewModel.createPlaylist(name, description, image)
                showToast(name)
                findNavController().navigateUp()
            }
        }
    }

    private fun showToast(playlistName: String){
        val toast = Toast(requireContext())

        val layout = layoutInflater.inflate(
            R.layout.playlist_toast,
            null
        )
        val textView = layout.findViewById<TextView>(R.id.info)
        val info =
            requireContext().getString(R.string.playlist) + " " + playlistName + " " + requireContext().getString(R.string.created)
        textView.setText(info)

        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }

    fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            requireContext().resources.displayMetrics).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}