package com.example.vinilos.ui.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.data.model.Album
import com.example.vinilos.databinding.AlbumListItemBinding

class AlbumAdapter(private val onItemClicked: (Album) -> Unit) : ListAdapter<Album, AlbumAdapter.AlbumViewHolder>(AlbumDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = AlbumListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AlbumViewHolder(
        private val binding: AlbumListItemBinding,
        private val onItemClicked: (Album) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.albumNameText.text = album.name

            val artistName = album.performers?.firstOrNull()?.name ?: itemView.context.getString(R.string.unknown_album)
            binding.albumArtistText.text = artistName

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

class AlbumDiffCallback : DiffUtil.ItemCallback<Album>() {
    override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem == newItem
    }
}