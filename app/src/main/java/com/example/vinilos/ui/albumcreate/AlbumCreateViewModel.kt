package com.example.vinilos.ui.albumcreate

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.vinilos.data.model.AlbumCreate
import com.example.vinilos.data.repository.AlbumRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class AlbumCreateViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AlbumRepository()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _creationSuccess = MutableLiveData<Boolean>()
    val creationSuccess: LiveData<Boolean> = _creationSuccess

    fun createAlbum(
        name: String,
        cover: String,
        releaseDateStr: String,
        description: String,
        genre: String,
        recordLabel: String
    ) {
        // Basic Validation
        if (name.isBlank()) {
            _error.value = "El nombre del álbum es requerido."
            return
        }
        if (cover.isBlank()) {
            _error.value = "La URL de la portada es requerida."
            return
        }
        if (releaseDateStr.isBlank()) {
            _error.value = "La fecha de lanzamiento es requerida."
            return
        }
        if (description.isBlank()) {
            _error.value = "La descripción es requerida."
            return
        }
        if (genre.isBlank()) {
            _error.value = "El género es requerido."
            return
        }
        if (recordLabel.isBlank()) {
            _error.value = "El sello discográfico es requerido."
            return
        }

        val backendDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        backendDateFormat.timeZone = TimeZone.getTimeZone("UTC")

        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date: Date? = try {
            inputDateFormat.parse(releaseDateStr)
        } catch (e: Exception) {
            Log.e("AlbumCreateViewModel", "Date parsing error: ${e.message}")
            _error.value = "Formato de fecha inválido. Use YYYY-MM-DD."
            return
        }

        if (date == null) {
            _error.value = "Formato de fecha inválido. Use YYYY-MM-DD."
            return
        }

        val formattedReleaseDate = backendDateFormat.format(date)

        val album = AlbumCreate(
            name = name,
            cover = cover,
            releaseDate = formattedReleaseDate,
            description = description,
            genre = genre,
            recordLabel = recordLabel
        )

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _creationSuccess.value = false
            try {
                val result = repository.createAlbum(album)
                if (result != null) {
                    _creationSuccess.value = true
                } else {
                    _error.value = "Error al crear el álbum."
                }
            } catch (e: Exception) {
                Log.e("AlbumCreateViewModel", "Exception creating album: ${e.message}", e)
                _error.value = "Excepción creando álbum: ${e.message}"
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