package com.example.vinilos.ui.collectordetail

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
import com.example.vinilos.R
import com.example.vinilos.databinding.FragmentCollectorDetailBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import java.net.URLEncoder
import androidx.recyclerview.widget.DividerItemDecoration

class CollectorDetailFragment : Fragment() {

    private var _binding: FragmentCollectorDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CollectorDetailViewModel
    private val args: CollectorDetailFragmentArgs by navArgs()
    private lateinit var collectorAlbumAdapter: CollectorAlbumAdapter
    private lateinit var commentAdapter: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("CollectorDetailFragment", "onViewCreated for collector ID: ${args.collectorIdArg}")

        viewModel = ViewModelProvider(this).get(CollectorDetailViewModel::class.java)
        commentAdapter = CommentAdapter()
        setupAlbumCollectorRecyclerView()
        setupCommentCollectorRecycledView()
        observeViewModel()
    }

    private fun setupAlbumCollectorRecyclerView() {
        collectorAlbumAdapter = CollectorAlbumAdapter { album ->
            Log.d("CollectorDetailFragment", "Album clicked: ID=${album.id}, Name=${album.name}")
            val action = CollectorDetailFragmentDirections.actionDetailCollectorToAlbum(
                albumIdArg = album.id,
                albumNameArg = album.name
            )
            findNavController().navigate(action)
        }

        binding.detailCollectorAlbumsRecycledView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = collectorAlbumAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupCommentCollectorRecycledView(){
        binding.detailCollectionCommentsRecycledView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = commentAdapter
            isNestedScrollingEnabled = false

            val divider = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            addItemDecoration(divider)
        }
    }

    private fun observeViewModel() {
        viewModel.collectorDetail.observe(viewLifecycleOwner) { collector ->
            collector?.let {
                Log.d("CollectorDetailFragment", "Collector data received: ${it.name}")
                binding.detailCollectorName.text = it.name
                binding.detailCollectorPhone.text = binding.root.context.getString(R.string.collector_phone, it.telephone)
                binding.detailCollectorEmail.text = binding.root.context.getString(R.string.collector_email, it.email)

                val encodedName = URLEncoder.encode(collector.name, "UTF-8")
                val avatarUrl = "https://api.dicebear.com/9.x/miniavs/png?seed=$encodedName"

                Glide.with(this)
                    .load(avatarUrl)
                    .error(R.drawable.artist_24px)
                    .transform(CircleCrop())
                    .into(binding.detailCollectorCover)

                if (collector.comments.isNullOrEmpty()){
                    Log.d("CollectorDetailFragment", "Comments are null or empty")
                    commentAdapter.submitList(emptyList())
                } else {
                    Log.d("CollectorDetailFragment", "Submitting ${collector.comments.size} comments to adapter")
                    commentAdapter  .submitList(collector.comments)
                }

                (activity as? AppCompatActivity)?.supportActionBar?.title = it.name

            } ?: Log.d("AlbumDetailFragment", "Received null Collector data")
        }

        viewModel.albums.observe(viewLifecycleOwner) { albums ->
            albums?.let {
                Log.d("CollectorDetailFragment", "Albums data received")

                if (albums.isEmpty()){
                    Log.d("AlbumDetailFragment", "Tracks are null or empty")
                    collectorAlbumAdapter.submitList(emptyList())
                } else {
                    Log.d("AlbumDetailFragment", "Submitting ${it.size} tracks to adapter")
                    collectorAlbumAdapter.submitList(it)
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Log.d("AlbumDetailFragment", "isLoading changed: $isLoading")
            binding.albumDetailLoadingSpiner.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.contentScrollView.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Log.d("AlbumDetailFragment", "Error observed: $error")
            binding.collectorDetailErrorText.visibility = if (error != null) View.VISIBLE else View.GONE
            binding.collectorDetailErrorText.text = error
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