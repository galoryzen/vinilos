package com.example.vinilos.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vinilos.databinding.FragmentHomeBinding
import com.example.vinilos.ui.SharedViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonVisitor.setOnClickListener {
            sharedViewModel.setGuestStatus(true)
            val action = HomeFragmentDirections.actionNavigationHomeToNavigationAlbum()
            findNavController().navigate(action)
        }

        binding.buttonCollector.setOnClickListener {
            sharedViewModel.setGuestStatus(false)
            val action = HomeFragmentDirections.actionNavigationHomeToNavigationCollector()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}