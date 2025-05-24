package com.example.vinilos.data.repository

import android.util.Log
import com.example.vinilos.data.cache.CacheManager
import com.example.vinilos.data.model.Album
import com.example.vinilos.data.model.AlbumCreate
import com.example.vinilos.data.network.RetrofitInstance

class AlbumRepository {

    private val apiService = RetrofitInstance.api

    suspend fun getAllAlbums(): List<Album>? {
        val cachedAlbums = CacheManager.getAlbumsList()
        if (cachedAlbums != null) {
            Log.d("AlbumRepository", "Using cached albums data")
            return cachedAlbums
        }

        Log.d("AlbumRepository", "Fetching albums data from API")
        try {
            val response = apiService.getAlbums()
            if (response.isSuccessful) {
                Log.d("AlbumRepository", "Data received: ${response.body()}")
                val albums = response.body()
                albums?.let { CacheManager.putAlbumsList(it) }
                return albums
            } else {
                Log.e("AlbumRepository", "API Error Response: ${response.errorBody()?.string()}")
                return null
            }
        } catch (e: Exception) {
            Log.e("AlbumRepository", "Network Exception: ${e.message}", e)
            return null
        }
    }

    suspend fun getAlbumDetail(albumId: Int): Album? {
        val cachedAlbum = CacheManager.getAlbumDetail(albumId)
        if (cachedAlbum != null) {
            Log.d("AlbumRepository", "Using cached album detail data")
            return cachedAlbum
        }

        Log.d("AlbumRepository", "Fetching album detail data from API")
        try {
            val response = apiService.getAlbumDetail(albumId)
            if (response.isSuccessful) {
                val albumDetail = response.body()
                Log.d("AlbumRepository", "Detail Data received: ${response.body()}")
                albumDetail?.let { CacheManager.putAlbumDetail(albumId, it) }
                return albumDetail
            } else {
                Log.e("AlbumRepository", "Detail API Error: ${response.errorBody()?.string()}")
                return null
            }
        } catch (e: Exception) {
            Log.e("AlbumRepository", "Detail Network Exception: ${e.message}", e)
            return null
        }
    }

    suspend fun createAlbum(album: AlbumCreate): Album? {
        try {
            val response = apiService.createAlbum(album)
            if (response.isSuccessful) {
                val newAlbum = response.body()
                Log.d("AlbumRepository", "Album created: $newAlbum")

                newAlbum?.let {
                    // CLAVE: Invalidar cache para que se muestre en la lista
                    CacheManager.invalidateAlbumsListCache()
                    Log.d("AlbumRepository", "Album list Cache invalidated")

                    // Actualizar el cache de detail con el nuevo álbum
                    CacheManager.putAlbumDetail(newAlbum.id, newAlbum)
                }

                return newAlbum
            } else {
                Log.e("AlbumRepository", "Create API Error: ${response.errorBody()?.string()}")
                return null
            }
        } catch (e: Exception) {
            Log.e("AlbumRepository", "Create Network Exception: ${e.message}", e)
            return null
        }
    }
}