package com.example.vinilos.ui.artist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vinilos.data.model.Artist
import com.example.vinilos.data.repository.ArtistRepository
import kotlinx.coroutines.launch

class ArtistViewModel : ViewModel() {

    private val repository = ArtistRepository()

    private val _artists = MutableLiveData<List<Artist>?>()
    val artists: MutableLiveData<List<Artist>?> = _artists

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        if (_artists.value.isNullOrEmpty()) {
            fetchArtists()
        }
    }

    private fun fetchArtists() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = repository.getAllArtists()
                if (result != null) {
                    _artists.value = result
                } else {
                    _error.value = "Error al cargar los artistas"
                }
            } catch (e: Exception) {
                _error.value = "Excepción: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}