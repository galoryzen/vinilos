package com.example.vinilos.ui.artistdetail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.data.model.Album
import com.example.vinilos.databinding.AlbumArtistDetailBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class AlbumArtistAdapter : ListAdapter<Album, AlbumArtistAdapter.AlbumArtistViewHolder>(AlbumArtistDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumArtistViewHolder {
        val binding = AlbumArtistDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumArtistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumArtistViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AlbumArtistViewHolder(private val binding: AlbumArtistDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.albumNameText.text = album.name
            binding.albumYearText.text = getFormattedArtistYear(album)
            Glide.with(binding.root.context)
                .load(album.cover)
                .placeholder(R.drawable.baseline_album_24)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.albumCoverImage)
        }
    }

    fun getFormattedArtistYear(album: Album?): String {
        val year = try {
            album?.releaseDate?.let {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                val date: Date? = inputFormat.parse(it)
                val outputFormat = SimpleDateFormat("yyyy", Locale.getDefault())
                date?.let { d -> outputFormat.format(d) } ?: ""
            } ?: ""
        } catch (e: Exception) {
            Log.e("AlbumArtistAdapter", "Error parsing date: ${album?.releaseDate}", e)
            ""
        }

        return year.ifEmpty { "" }
    }
}

class AlbumArtistDiffCallback : DiffUtil.ItemCallback<Album>() {
    override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem == newItem
    }
}