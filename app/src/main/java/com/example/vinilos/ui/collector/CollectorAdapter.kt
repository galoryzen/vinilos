package com.example.vinilos.ui.collector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.vinilos.R
import com.example.vinilos.data.model.Collector
import com.example.vinilos.databinding.CollectorListItemBinding
import java.net.URLEncoder

class CollectorAdapter(private val onItemClicked: (Collector) -> Unit) :
    ListAdapter<Collector, CollectorAdapter.CollectorViewHolder>(CollectorDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorViewHolder {
        val binding = CollectorListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectorViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: CollectorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CollectorViewHolder(
        private val binding: CollectorListItemBinding,
        private val onItemClicked: (Collector) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(collector: Collector) {
            binding.collectorNameText.text = collector.name
            binding.collectorEmailText.text = collector.email ?: itemView.context.getString(R.string.no_email_provided)

            val encodedName = URLEncoder.encode(collector.name, "UTF-8")
            val avatarUrl = "https://api.dicebear.com/9.x/miniavs/png?seed=$encodedName"

            Glide.with(binding.root.context)
                .load(avatarUrl)
                .error(R.drawable.artist_24px)
                .transform(CircleCrop())
                .into(binding.collectorIcon)

            itemView.setOnClickListener {
                onItemClicked(collector)
            }
        }
    }
}

class CollectorDiffCallback : DiffUtil.ItemCallback<Collector>() {
    override fun areItemsTheSame(oldItem: Collector, newItem: Collector): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Collector, newItem: Collector): Boolean {
        return oldItem == newItem
    }
}