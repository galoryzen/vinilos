package com.example.vinilos.ui.collector

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vinilos.R
import com.example.vinilos.databinding.FragmentCollectorBinding

class CollectorFragment : Fragment() {

    private var _binding: FragmentCollectorBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CollectorViewModel
    private var collectorAdapter: CollectorAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("CollectorFragment", "onCreateView called")
        _binding = FragmentCollectorBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(CollectorViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("CollectorFragment", "onViewCreated called")
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        Log.d("CollectorFragment", "setupRecyclerView called")

        collectorAdapter = CollectorAdapter { collector ->
            Log.d("CollectorFragment", "Collector clicked: ID=${collector.id}, Name=${collector.name}")

            // TODO: Aqui va el código para navegar a la vista de detalle de coleccionista
            Toast.makeText(context, "Clicked: ${collector.name}", Toast.LENGTH_SHORT).show()
        }

        binding.collectorsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = collectorAdapter
            setHasFixedSize(true)
        }
        Log.d("CollectorFragment", "RecyclerView setup finished")
    }

    private fun observeViewModel() {
        Log.d("CollectorFragment", "observeViewModel called")

        viewModel.collectors.observe(viewLifecycleOwner) { collectors ->
            Log.d("CollectorFragment", "Collectors LiveData observed with ${collectors?.size ?: 0} items.")
            collectors?.let {
                if (it.isEmpty()) {
                    Log.d("CollectorFragment", "Collector list is empty.")
                    binding.errorText.text = getString(R.string.no_collectors_found)
                    binding.errorText.visibility = View.VISIBLE
                } else {
                    binding.errorText.visibility = View.GONE
                }
                collectorAdapter?.submitList(it)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Log.d("CollectorFragment", "isLoading LiveData observed: $isLoading")
            binding.loadingSpinner.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.collectorsRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Log.d("CollectorFragment", "Error LiveData observed: $errorMessage")
            if (errorMessage != null) {
                binding.errorText.text = errorMessage
                binding.errorText.visibility = View.VISIBLE
                binding.collectorsRecyclerView.visibility = View.GONE
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            } else {
                binding.errorText.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("CollectorFragment", "onDestroyView called")
        binding.collectorsRecyclerView.adapter = null
        collectorAdapter = null
        _binding = null
    }
}