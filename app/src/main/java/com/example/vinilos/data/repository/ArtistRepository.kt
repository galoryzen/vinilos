package com.example.vinilos.data.repository

import android.util.Log
import com.example.vinilos.data.cache.CacheManager
import com.example.vinilos.data.model.Artist
import com.example.vinilos.data.network.RetrofitInstance

class ArtistRepository {

    private val apiService = RetrofitInstance.api

    suspend fun getAllArtists(): List<Artist>? {
        val cachedArtists = CacheManager.getArtistsList()
        if (cachedArtists != null) {
            Log.d("ArtistRepository", "Using cached artists data")
            return cachedArtists
        }

        Log.d("ArtistRepository", "Fetching artists data from API")
        try {
            val response = apiService.getArtists()
            if (response.isSuccessful) {
                val artists = response.body()
                Log.d("ArtistRepository", "Data received: $artists")
                artists?.let { CacheManager.putArtistsList(it) }
                return artists
            } else {
                Log.e("ArtistRepository", "API Error Response: ${response.errorBody()?.string()}")
                return null
            }
        } catch (e: Exception) {
            Log.e("ArtistRepository", "Network Exception: ${e.message}", e)
            return null
        }
    }

    suspend fun getArtistDetail(artistId: Int): Artist? {
        val cachedArtist = CacheManager.getArtistDetail(artistId)
        if (cachedArtist != null) {
            Log.d("ArtistRepository", "Using cached detail data")
            return cachedArtist
        }

        Log.d("ArtistRepository", "Fetching artist detail data from API")
        try {
            val response = apiService.getArtistDetail(artistId)
            if (response.isSuccessful) {
                val artistDetail = response.body()
                Log.d("ArtistRepository", "Detail Data received: $artistDetail")
                artistDetail?.let { CacheManager.putArtistDetail(artistId, it) }
                return artistDetail
            } else {
                Log.e("ArtistRepository", "Detail API Error: ${response.errorBody()?.string()}")
                return null
            }
        } catch (e: Exception) {
            Log.e("ArtistRepository", "Detail Network Exception: ${e.message}", e)
            return null
        }
    }
}