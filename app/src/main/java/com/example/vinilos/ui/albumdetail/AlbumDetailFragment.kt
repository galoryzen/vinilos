package com.example.vinilos.ui.albumdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.databinding.FragmentAlbumDetailBinding

class AlbumDetailFragment : Fragment() {

    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AlbumDetailViewModel
    private val args: AlbumDetailFragmentArgs by navArgs()
    private lateinit var trackAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        trackAdapter = TrackAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("AlbumDetailFragment", "onViewCreated for album ID: ${args.albumIdArg}")

        viewModel = ViewModelProvider(this).get(AlbumDetailViewModel::class.java)

        setupTrackRecyclerView()
        observeViewModel()
    }

    private fun setupTrackRecyclerView() {
        binding.detailTracksRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = trackAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun observeViewModel() {
        viewModel.albumDetail.observe(viewLifecycleOwner) { album ->
            album?.let {
                Log.d("AlbumDetailFragment", "Album data received: ${it.name}")
                binding.detailAlbumTitle.text = it.name
                binding.detailAlbumArtistYear.text = viewModel.getFormattedArtistYear(it)
                binding.detailAlbumDescription.text = it.description ?: ""

                (activity as? AppCompatActivity)?.supportActionBar?.title = it.name

                Glide.with(this)
                    .load(it.cover)
                    .placeholder(R.drawable.baseline_album_24)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(binding.detailAlbumCover)

                if (it.tracks.isNullOrEmpty()){
                    Log.d("AlbumDetailFragment", "Tracks are null or empty")
                    trackAdapter.submitList(emptyList())
                } else {
                    Log.d("AlbumDetailFragment", "Submitting ${it.tracks.size} tracks to adapter")
                    trackAdapter.submitList(it.tracks)
                }

            } ?: Log.d("AlbumDetailFragment", "Received null Album data")
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Log.d("AlbumDetailFragment", "isLoading changed: $isLoading")
            binding.detailLoadingSpinner.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.contentScrollView.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Log.d("AlbumDetailFragment", "Error observed: $error")
            binding.detailErrorText.visibility = if (error != null) View.VISIBLE else View.GONE
            binding.detailErrorText.text = error
            if (error != null) {
                binding.contentScrollView.visibility = View.GONE
            }
        }
    }

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