package com.example.vinilos.ui.collector

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vinilos.data.model.Collector
import com.example.vinilos.data.repository.CollectorRepository
import kotlinx.coroutines.launch

class CollectorViewModel : ViewModel() {

    private val repository = CollectorRepository()

    private val _collectors = MutableLiveData<List<Collector>>()
    val collectors: LiveData<List<Collector>> = _collectors

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        fetchCollectors()
    }

    @SuppressLint("NullSafeMutableLiveData")
    private fun fetchCollectors() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = repository.getAllCollectors()
                if (result != null) {
                    _collectors.value = result
                } else {
                    _error.value = "Error al cargar los coleccionistas"
                }
            } catch (e: Exception) {
                _error.value = "Excepción: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}