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
import com.example.vinilos.databinding.FragmentHomeBinding
import com.example.vinilos.ui.collector.CollectorAdapter.CollectorViewHolder

class HomeAdapter(private val onItemClicked: () -> Unit) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = FragmentHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding, onItemClicked)
    }

    inner class HomeViewHolder(
        private val binding: FragmentHomeBinding,
        private val onItemClicked: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            itemView.setOnClickListener {
                onItemClicked()
            }
        }
    }



    /**
     * Replaces the content of an existing view with new data
     */
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}