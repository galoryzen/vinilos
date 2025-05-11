package com.example.vinilos.ui.artistdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.databinding.FragmentArtistDetailBinding

class ArtistDetailFragment : Fragment() {

    private var _binding: FragmentArtistDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ArtistDetailViewModel
    private val args: ArtistDetailFragmentArgs by navArgs()
    private lateinit var albumArtistAdapter: AlbumArtistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistDetailBinding.inflate(inflater, container, false)
        albumArtistAdapter = AlbumArtistAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ArtistDetailFragment", "onViewCreated for artist ID: ${args.artistIdArg}")

        viewModel = ViewModelProvider(this).get(ArtistDetailViewModel::class.java)

        setupTrackRecyclerView()
        observeViewModel()
    }

    private fun setupTrackRecyclerView() {
        binding.detailAlbumsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = albumArtistAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun observeViewModel() {
        viewModel.artistDetail.observe(viewLifecycleOwner) { artist ->
            artist?.let {
                Log.d("ArtistDetailFragment", "Artist data received: ${it.name}")
                binding.detailArtistTitle.text = it.name
                binding.detailArtistYear.text = viewModel.getFormattedArtistYear(it)
                binding.detailArtistDescription.text = it.description ?: ""

                (activity as? AppCompatActivity)?.supportActionBar?.title = it.name

                Glide.with(this)
                    .load(it.image)
                    .placeholder(R.drawable.baseline_album_24)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(binding.detailArtistCover)

                if (it.albums.isNullOrEmpty()){
                    Log.d("ArtistDetailFragment", "Albums are null or empty")
                    albumArtistAdapter.submitList(emptyList())
                } else {
                    Log.d("ArtistDetailFragment", "Submitting ${it.albums.size} albums to adapter")
                    albumArtistAdapter.submitList(it.albums)
                }

            } ?: Log.d("ArtistDetailFragment", "Received null Artist data")
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Log.d("ArtistDetailFragment", "isLoading changed: $isLoading")
            binding.detailLoadingSpinner.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.contentScrollView.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Log.d("ArtistDetailFragment", "Error observed: $error")
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