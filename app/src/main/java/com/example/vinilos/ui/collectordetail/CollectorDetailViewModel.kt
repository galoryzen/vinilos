package com.example.vinilos.ui.collectordetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vinilos.data.model.CollectedAlbumAuction
import com.example.vinilos.data.model.Collector
import com.example.vinilos.data.model.AlbumCollector
import com.example.vinilos.data.repository.CollectorRepository
import kotlinx.coroutines.launch

class CollectorDetailViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val repository = CollectorRepository()

    private val _collectorDetail = MutableLiveData<Collector>()
    val collectorDetail: LiveData<Collector> = _collectorDetail

    private val _albums = MutableLiveData<List<AlbumCollector>>()
    val albums: LiveData<List<AlbumCollector>> = _albums

    private val collectorId: Int = savedStateHandle.get<Int>("collectorIdArg")!!

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        fetchCollectorDetail(collectorId)
    }

    private fun fetchCollectorDetail(collectorId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.getCollectorDetail(collectorId)?.let {
                    _collectorDetail.value = it
                } ?: run {
                    _error.value = "Error al cargar el coleccionista "
                }

                repository.getCollectorDetailWithAlbums(collectorId)?.let { auctions ->
                    val albumsCollector = auctions.map {auction -> auction.album}

                    _albums.value = albumsCollector
                } ?: run {
                    _error.value = "Error al cargar los albumes del coleccionista "
                }
            } catch (e: Exception) {
                _error.value = "Excepción: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}