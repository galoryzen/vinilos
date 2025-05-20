package com.example.vinilos.ui.matchtrackalbum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.vinilos.R
import com.example.vinilos.databinding.FragmentAlbumTrackMatchBinding

class AlbumTrackSyncFragment : Fragment() {

    private var _binding: FragmentAlbumTrackMatchBinding? = null
    private val binding get() = _binding!!
    private val args: AlbumTrackSyncFragmentArgs by navArgs()

    private lateinit var viewModel: AlbumTrackSyncViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumTrackMatchBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AlbumTrackSyncViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.title_create_album)

        binding.buttonSaveTrack.setOnClickListener {
            val name = binding.etTrackName.text.toString()
            val duration = binding.etTrackLength.text.toString()
            val album = args.albumIdArg


            viewModel.createTrack(
                albumId = album,
                trackName = name,
                duration = duration,
            )
        }

        observeViewModel()
    }


    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.createSyncLoadingSpinner.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.buttonSaveTrack.isEnabled = !isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, getString(R.string.track_album_creation_failed, it), Toast.LENGTH_LONG).show()
                viewModel.resetCreationStatus()
            }
        }

        viewModel.creationSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, getString(R.string.album_created_success), Toast.LENGTH_SHORT).show()

                findNavController().previousBackStackEntry?.savedStateHandle?.set("track_created", true)

                findNavController().navigateUp()
                viewModel.resetCreationStatus()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}