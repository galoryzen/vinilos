package com.example.vinilos.data.repository

import android.util.Log
import com.example.vinilos.data.model.Album
import com.example.vinilos.data.network.RetrofitInstance

class AlbumRepository {

    private val apiService = RetrofitInstance.api

    suspend fun getAllAlbums(): List<Album>? {
        try {
            val response = apiService.getAlbums()
            if (response.isSuccessful) {
                Log.d("AlbumRepository", "Data received: ${response.body()}")
                return response.body()
            } else {
                Log.e("AlbumRepository", "API Error Response: ${response.errorBody()?.string()}")
                return null
            }
        } catch (e: Exception) {
            Log.e("AlbumRepository", "Network Exception: ${e.message}", e)
            return null
        }
    }
}