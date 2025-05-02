package com.example.vinilos.ui.collectordetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.data.model.AlbumCollector
import com.example.vinilos.databinding.AlbumListItemBinding

class CollectorAlbumAdapter(
    private val onItemClicked: (AlbumCollector) -> Unit
) : ListAdapter<AlbumCollector, CollectorAlbumAdapter.CollectorAlbumViewHolder>(AlbumCollectorDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorAlbumViewHolder {
        val binding = AlbumListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectorAlbumViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: CollectorAlbumViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CollectorAlbumViewHolder(
        private val binding: AlbumListItemBinding,
        private val onItemClicked: (AlbumCollector) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(album: AlbumCollector) {
            binding.albumNameText.text = album.name
            binding.albumArtistText.text = album.description?.let {
                if (it.length > 50) {
                    it.take(50) + "..."
                } else {
                    it
                }
            } ?: ""

            itemView.setOnClickListener {
                onItemClicked(album)
            }

            Glide.with(binding.root.context)
                .load(album.cover)
                .placeholder(R.drawable.baseline_album_24)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.albumCoverImage)
        }
    }
}

class AlbumCollectorDiffCallback : DiffUtil.ItemCallback<AlbumCollector>() {
    override fun areItemsTheSame(oldItem: AlbumCollector, newItem: AlbumCollector): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AlbumCollector, newItem: AlbumCollector): Boolean {
        return oldItem == newItem
    }
}
