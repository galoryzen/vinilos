package com.example.vinilos.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vinilos.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // We don't need the ViewModel for this simple welcome screen
        // val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Remove TextView observation as per new layout
        // val textView: TextView = binding.textHome
        // homeViewModel.text.observe(viewLifecycleOwner) {
        //     textView.text = it
        // }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up button click listeners for navigation
        binding.buttonVisitor.setOnClickListener {
            // Navigate to the Album list for Visitors
            val action = HomeFragmentDirections.actionNavigationHomeToNavigationAlbum()
            findNavController().navigate(action)
        }

        binding.buttonCollector.setOnClickListener {
            // Navigate to the Collector list for Collectors
            val action = HomeFragmentDirections.actionNavigationHomeToNavigationCollector()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}