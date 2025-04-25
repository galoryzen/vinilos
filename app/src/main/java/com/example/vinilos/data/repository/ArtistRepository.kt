package com.example.vinilos.data.repository

import android.util.Log
import com.example.vinilos.data.model.Artist
import com.example.vinilos.data.network.RetrofitInstance

class ArtistRepository {

    private val apiService = RetrofitInstance.api

    suspend fun getAllArtists(): List<Artist>? {
        try {
            val response = apiService.getArtists()
            if (response.isSuccessful) {
                Log.d("ArtistRepository", "Data received: ${response.body()}")
                return response.body()
            } else {
                Log.e("ArtistRepository", "API Error Response: ${response.errorBody()?.string()}")
                return null
            }
        } catch (e: Exception) {
            Log.e("ArtistRepository", "Network Exception: ${e.message}", e)
            return null
        }
    }
}