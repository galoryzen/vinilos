package com.example.vinilos.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos.R
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.vinilos.data.model.Collector
import com.example.vinilos.databinding.FragmentCollectorBinding
import com.example.vinilos.databinding.FragmentHomeBinding
import com.example.vinilos.ui.collector.CollectorAdapter.CollectorViewHolder

class HomeAdapter(private val onItemClicked: (Collector) -> Unit) :
    ListAdapter<Collector, HomeAdapter.HomeViewHolder>(CollectorsDiffCallback())
    {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = FragmentCollectorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding, onItemClicked)
    }

    inner class HomeViewHolder(
        private val binding: FragmentCollectorBinding,
        private val onItemClicked: (Collector) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(collector: Collector) {
            itemView.setOnClickListener {
                onItemClicked(collector)
            }
        }
    }

    /**
     * Replaces the content of an existing view with new data
     */
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}

class CollectorsDiffCallback : DiffUtil.ItemCallback<Collector>() {
    override fun areItemsTheSame(oldItem: Collector, newItem: Collector): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Collector, newItem: Collector): Boolean {
        return oldItem == newItem
    }
}