package com.example.vinilos.ui.album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vinilos.R
import com.example.vinilos.databinding.FragmentAlbumBinding
import androidx.navigation.fragment.findNavController
import com.example.vinilos.ui.SharedViewModel

class AlbumFragment : Fragment() {

    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AlbumViewModel

    private var albumAdapter: AlbumAdapter? = null

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.d("AlbumFragment", "onCreateView called")
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AlbumViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.isGuest.observe(viewLifecycleOwner) { isGuest ->
            Log.d("AlbumFragment", "isGuest observed: $isGuest")
            if (isGuest) {
                Log.d("AlbumFragment", "User is a guest, hiding create album button")
                binding.buttonGoToCreateAlbum.visibility = View.GONE
            } else {
                Log.d("AlbumFragment", "User is a collector, showing create album button")
                binding.buttonGoToCreateAlbum.visibility = View.VISIBLE
            }
        }

        Log.d("AlbumFragment", "onViewCreated called")
        setupRecyclerView()
        observeViewModel()

        binding.buttonGoToCreateAlbum.setOnClickListener {
            Log.d("AlbumFragment", "Create Album button clicked")
            val action = AlbumFragmentDirections.actionNavigationAlbumToCreateAlbum()
            findNavController().navigate(action)
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("album_created")
            ?.observe(viewLifecycleOwner) { isAlbumCreated ->
                if (isAlbumCreated) {
                    Log.d("AlbumFragment", "Album created, refreshing list")
                    viewModel.refreshAlbums()
                    findNavController().currentBackStackEntry?.savedStateHandle?.remove<Boolean>("album_created")
                }
            }
    }

    private fun setupRecyclerView() {
        Log.d("AlbumFragment", "setupRecyclerView called")

        albumAdapter = AlbumAdapter { album ->
            Log.d("AlbumFragment", "Album clicked: ID=${album.id}, Name=${album.name}")
            val action = AlbumFragmentDirections.actionListToDetail(
                albumIdArg = album.id, albumNameArg = album.name
            )
            findNavController().navigate(action)
        }

        binding.albumsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = albumAdapter
            setHasFixedSize(true)
        }
        Log.d("AlbumFragment", "RecyclerView setup finished")
    }

    private fun observeViewModel() {
        Log.d("AlbumFragment", "observeViewModel called")

        viewModel.albums.observe(viewLifecycleOwner) { albums ->
            Log.d("AlbumFragment", "Albums LiveData observed with ${albums?.size ?: 0} items.")
            albums?.let {
                if (it.isEmpty()) {
                    Log.d("AlbumFragment", "Album list is empty.")
                    binding.errorText.text = getString(R.string.no_albums_found)
                    binding.errorText.visibility = View.VISIBLE
                } else {
                    binding.errorText.visibility = View.GONE
                }
                albumAdapter?.submitList(it)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Log.d("AlbumFragment", "isLoading LiveData observed: $isLoading")
            binding.loadingSpinner.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.albumsRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Log.d("AlbumFragment", "Error LiveData observed: $errorMessage")
            if (errorMessage != null) {
                binding.errorText.text = errorMessage
                binding.errorText.visibility = View.VISIBLE
                binding.albumsRecyclerView.visibility = View.GONE
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            } else {
                binding.errorText.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("AlbumFragment", "onDestroyView called")
        _binding = null
    }
}