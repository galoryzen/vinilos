package com.example.vinilos.ui.artist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.data.model.Artist
import com.example.vinilos.databinding.ArtistListItemBinding

class ArtistAdapter(private val onItemClicked: (Artist) -> Unit) :
    ListAdapter<Artist, ArtistAdapter.ArtistViewHolder>(ArtistDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding = ArtistListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ArtistViewHolder(
        private val binding: ArtistListItemBinding,
        private val onItemClicked: (Artist) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(artist: Artist) {
            binding.artistNameText.text = artist.name

            val artistName = artist.performers?.firstOrNull()?.name ?: itemView.context.getString(R.string.unknown_artist)
            binding.artistAlbumText.text = artistName

            itemView.setOnClickListener {
                onItemClicked(artist)
            }

            Glide.with(binding.root.context)
                .load(artist.image)
                .placeholder(R.drawable.baseline_album_24)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.artistCoverImage)
        }
    }
}

class ArtistDiffCallback : DiffUtil.ItemCallback<Artist>() {
    override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
        return oldItem == newItem
    }
}