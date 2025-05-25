package com.example.vinilos.ui.albumdetail

import android.util.Log
import androidx.lifecycle.*
import com.example.vinilos.data.model.Album
import com.example.vinilos.data.repository.AlbumRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AlbumDetailViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val repository = AlbumRepository()

    private val _albumDetail = MutableLiveData<Album?>()
    val albumDetail: LiveData<Album?> = _albumDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val albumId: Int = savedStateHandle.get<Int>("albumIdArg")!!

    init {
        Log.d("AlbumDetailViewModel", "Fetching detail for album ID: $albumId")
        fetchAlbumDetails(albumId)
    }

    fun fetchAlbum() {
        fetchAlbumDetails(albumId)
    }

    private fun fetchAlbumDetails(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = repository.getAlbumDetail(id)
                _albumDetail.value = result
                if (result == null && _error.value == null) {
                    _error.value = "No se pudo obtener el detalle del álbum."
                }
            } catch (e: Exception) {
                Log.e("AlbumDetailViewModel", "Exception fetching details: ${e.message}", e)
                _error.value = "Excepción: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getFormattedArtistYear(album: Album?): String {
        val artistName = album?.performers?.firstOrNull()?.name ?: "Artista desconocido"
        val year = try {
            album?.releaseDate?.let {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                val date: Date? = inputFormat.parse(it)
                val outputFormat = SimpleDateFormat("yyyy", Locale.getDefault())
                date?.let { d -> outputFormat.format(d) } ?: ""
            } ?: ""
        } catch (e: Exception) {
            Log.e("AlbumDetailViewModel", "Error parsing date: ${album?.releaseDate}", e)
            ""
        }

        return if (year.isNotEmpty()) "$artistName | $year" else artistName
    }
}