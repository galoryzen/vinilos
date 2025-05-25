package com.example.vinilos.ui.artist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vinilos.R
import com.example.vinilos.databinding.FragmentArtistBinding

class ArtistFragment : Fragment() {

    private var _binding: FragmentArtistBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ArtistViewModel

    private var artistAdapter: ArtistAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("ArtistFragment", "onCreateView called")
        _binding = FragmentArtistBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ArtistViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ArtistFragment", "onViewCreated called")
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        Log.d("ArtistFragment", "setupRecyclerView called")

        artistAdapter = ArtistAdapter { artist ->
            // IMPORTANT: Add this check
            if (findNavController().currentDestination?.id == R.id.navigation_artist) {
                Log.d("ArtistFragment", "Artist clicked: ID=${artist.id}, Name=${artist.name}")
                val action = ArtistFragmentDirections.actionListToDetailArtist(
                    artistIdArg = artist.id,
                    artistNameArg = artist.name
                )
                findNavController().navigate(action)
            } else {
                Log.w("ArtistFragment", "Navigation to detail artist aborted: current destination is not ArtistFragment. Current dest: ${findNavController().currentDestination?.label}")
            }
        }

        binding.artistsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = artistAdapter
            setHasFixedSize(true)
        }
        Log.d("ArtistFragment", "RecyclerView setup finished")
    }

    private fun observeViewModel() {
        Log.d("ArtistFragment", "observeViewModel called")

        viewModel.artists.observe(viewLifecycleOwner) { artists ->
            Log.d("ArtistFragment", "Artist LiveData observed with ${artists?.size ?: 0} items.")
            artists?.let {
                if (it.isEmpty()) {
                    Log.d("ArtistFragment", "Artist list is empty.")
                    binding.errorText.text = getString(R.string.no_artists_found)
                    binding.errorText.visibility = View.VISIBLE
                } else {
                    binding.errorText.visibility = View.GONE
                }
                artistAdapter?.submitList(it)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Log.d("ArtistFragment", "isLoading LiveData observed: $isLoading")
            binding.loadingSpinner.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.artistsRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Log.d("ArtistFragment", "Error LiveData observed: $errorMessage")
            if (errorMessage != null) {
                binding.errorText.text = errorMessage
                binding.errorText.visibility = View.VISIBLE
                binding.artistsRecyclerView.visibility = View.GONE
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            } else {
                binding.errorText.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("ArtistFragment", "onDestroyView called")

        binding.artistsRecyclerView.adapter = null
        artistAdapter = null
        _binding = null
    }
}