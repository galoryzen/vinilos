package com.example.vinilos.ui.album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos.R
import com.example.vinilos.databinding.FragmentAlbumBinding
import androidx.navigation.fragment.findNavController

class AlbumFragment : Fragment() {

    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AlbumViewModel
    private var albumAdapter: AlbumAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("AlbumFragment", "onCreateView called")
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(AlbumViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("AlbumFragment", "onViewCreated called")
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        Log.d("AlbumFragment", "setupRecyclerView called")

        albumAdapter = AlbumAdapter { album ->
            Log.d("AlbumFragment", "Album clicked: ID=${album.id}, Name=${album.name}")
            val action = AlbumFragmentDirections.actionListToDetail(
                albumIdArg = album.id,
                albumNameArg = album.name
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