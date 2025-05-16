package com.example.vinilos.ui.albumcreate

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.vinilos.R
import com.example.vinilos.data.model.Genre
import com.example.vinilos.data.model.RecordLabel
import com.example.vinilos.databinding.FragmentAlbumCreateBinding
import java.text.SimpleDateFormat
import java.util.*

class AlbumCreateFragment : Fragment() {

    private var _binding: FragmentAlbumCreateBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AlbumCreateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumCreateBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AlbumCreateViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinners()
        setupDatePicker()

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.title_create_album)


        binding.buttonSaveAlbum.setOnClickListener {
            val name = binding.etAlbumName.text.toString()
            val cover = binding.etAlbumCover.text.toString()
            val releaseDate = binding.etAlbumReleaseDate.text.toString()
            val description = binding.etAlbumDescription.text.toString()
            val genre = binding.spinnerAlbumGenre.selectedItem as Genre
            val recordLabel = binding.spinnerAlbumRecordLabel.selectedItem as RecordLabel

            viewModel.createAlbum(
                name,
                cover,
                releaseDate,
                description,
                genre.toString(),
                recordLabel.toString()
            )
        }

        observeViewModel()
    }

    private fun setupSpinners() {
        val genreAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            Genre.entries.toTypedArray()
        )
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerAlbumGenre.adapter = genreAdapter

        val recordLabelAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            RecordLabel.entries.toTypedArray()
        )
        recordLabelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerAlbumRecordLabel.adapter = recordLabelAdapter
    }

    private fun setupDatePicker() {
        binding.etAlbumReleaseDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay)
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                    binding.etAlbumReleaseDate.setText(dateFormat.format(selectedDate.time))
                },
                year, month, day
            )
            datePickerDialog.show()
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.createAlbumLoadingSpinner.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.buttonSaveAlbum.isEnabled = !isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, getString(R.string.album_creation_failed, it), Toast.LENGTH_LONG).show()
                viewModel.resetCreationStatus()
            }
        }

        viewModel.creationSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, getString(R.string.album_created_success), Toast.LENGTH_SHORT).show()

                findNavController().previousBackStackEntry?.savedStateHandle?.set("album_created", true)

                findNavController().navigateUp()
                viewModel.resetCreationStatus()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}