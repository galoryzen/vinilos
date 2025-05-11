package com.example.vinilos.ui.artistdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.vinilos.data.model.Artist
import com.example.vinilos.data.repository.ArtistRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ArtistDetailViewModel (private val savedStateHandle: SavedStateHandle) : ViewModel(){
    private val repository = ArtistRepository()

    private val _artistDetail = MutableLiveData<Artist?>()
    val artistDetail: LiveData<Artist?> = _artistDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val artistId: Int = savedStateHandle.get<Int>("artistIdArg")!!

    init {
        Log.d("ArtistDetailViewModel", "Fetching detail for artist ID: $artistId")
        fetchArtistDetails(artistId)
    }

    private fun fetchArtistDetails(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = repository.getArtistDetail(id)
                _artistDetail.value = result
                if (result == null && _error.value == null) {
                    _error.value = "No se pudo obtener el detalle del artista."
                }
            } catch (e: Exception) {
                Log.e("ArtistDetailViewModel", "Exception fetching details: ${e.message}", e)
                _error.value = "Excepción: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getFormattedArtistYear(artist: Artist?): String {
        val year = try {
            artist?.creationDate?.let {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                val date: Date? = inputFormat.parse(it)
                val outputFormat = SimpleDateFormat("yyyy", Locale.getDefault())
                date?.let { d -> outputFormat.format(d) } ?: ""
            } ?: ""
        } catch (e: Exception) {
            Log.e("ArtistDetailViewModel", "Error parsing date: ${artist?.creationDate}", e)
            ""
        }

        return year.ifEmpty { "" }
    }
}