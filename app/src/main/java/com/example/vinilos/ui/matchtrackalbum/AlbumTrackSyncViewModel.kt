package com.example.vinilos.ui.matchtrackalbum

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.vinilos.data.repository.TrackRepository
import com.example.vinilos.data.model.TrackCreate
import kotlinx.coroutines.launch

class AlbumTrackSyncViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TrackRepository()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _creationSuccess = MutableLiveData<Boolean>()
    val creationSuccess: LiveData<Boolean> = _creationSuccess

    fun createTrack(
        albumId: Int,
        trackName: String,
        duration: String,
    ) {
        // Basic Validation
        if (trackName.isBlank()) {
            _error.value = "El nombre de la cancion es requerido."
            return
        }
        if (duration.isBlank()) {
            _error.value = "La duración es requerida."
            return
        }

        val durationPattern = Regex("^\\d{1,3}:\\d{2}\$")

        if (!duration.matches(durationPattern)) {
            _error.value = "La duración debe estar en el formato M:SS o MM:SS (por ejemplo, 1:20 o 15:45)."
            return
        }

        val secondsPart = duration.split(":").getOrNull(1)?.toIntOrNull()
        if (secondsPart == null || secondsPart >= 60) {
            _error.value = "Los segundos deben estar entre 00 y 59."
            return
        }

        val track = TrackCreate(
            name = trackName,
            duration = duration,
        )

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _creationSuccess.value = false
            try {
                val result = repository.createTrack(albumId, track)
                if (result != null) {
                    _creationSuccess.value = true
                } else {
                    _error.value = "Error al crear la cancion."
                }
            } catch (e: Exception) {
                Log.e("AlbumTrackSyncViewModel", "Exception creating track: ${e.message}", e)
                _error.value = "Excepción creando la cancion: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun resetCreationStatus() {
        _creationSuccess.value = false
        _error.value = null
    }
}