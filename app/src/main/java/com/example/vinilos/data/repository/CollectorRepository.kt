package com.example.vinilos.data.repository

import android.util.Log
import com.example.vinilos.data.cache.CacheManager
import com.example.vinilos.data.model.Collector
import com.example.vinilos.data.model.CollectedAlbumAuction
import com.example.vinilos.data.network.RetrofitInstance

class CollectorRepository {

    private val apiService = RetrofitInstance.api

    suspend fun getAllCollectors(): List<Collector>? {
        val cachedCollectors = CacheManager.getCollectorsList()
        if (cachedCollectors != null) {
            Log.d("CollectorRepository", "Using cached collectors data")
            return cachedCollectors
        }

        Log.d("CollectorRepository", "Fetching collectors data from API")
        try {
            val response = apiService.getCollectors()
            if (response.isSuccessful) {
                val collectors = response.body()
                Log.d("CollectorRepository", "Data received: $collectors")
                collectors?.let { CacheManager.putCollectorsList(it) }
                return collectors
            } else {
                Log.e("CollectorRepository", "API Error Response: ${response.errorBody()?.string()}")
                return null
            }
        } catch (e: Exception) {
            Log.e("CollectorRepository", "Network Exception: ${e.message}", e)
            return null
        }
    }

    suspend fun getCollectorDetail(id: Int): Collector? {
        val cachedCollector = CacheManager.getCollectorDetail(id)
        if (cachedCollector != null) {
            Log.d("CollectorRepository", "Using cached collector detail data")
            return cachedCollector
        }

        Log.d("CollectorRepository", "Fetching collector detail data from API")
        try{
            val response = apiService.getCollectorDetail(id)
            if (response.isSuccessful) {
                val collectorDetail = response.body()
                Log.d("CollectorRepository", "Detail Data received: $collectorDetail")
                collectorDetail?.let { CacheManager.putCollectorDetail(id, it) }
                return collectorDetail
            }

            Log.e("CollectionRepository", "API Error Response: ${response.errorBody()?.string()}")
            return null
        } catch(e: Exception){
            Log.e("CollectorRepisotry", "Network Exception: ${e.message}", e)
            return null
        }
    }

    suspend fun getCollectorDetailWithAlbums(id: Int): List<CollectedAlbumAuction>? {
        val cachedCollectorAlbums = CacheManager.getCollectorAlbums(id)
        if (cachedCollectorAlbums != null) {
            Log.d("CollectorRepository", "Using cached collector albums data")
            return cachedCollectorAlbums
        }

        Log.d("CollectorRepository", "Fetching collector albums data from API")
        try{
            val response = apiService.getCollectorDetailWithAlbums(id)
            if (response.isSuccessful) {
                val collectorAlbums = response.body()
                Log.d("CollectorRepository", "Detail with Albums Data received: $collectorAlbums")
                collectorAlbums?.let { CacheManager.putCollectorAlbums(id, it) }
                return collectorAlbums
            }

            Log.e("CollectionRepository", "API Error Response: ${response.errorBody()?.string()}")
            return null
        } catch(e: Exception){
            Log.e("CollectorRepisotry", "Network Exception: ${e.message}", e)
            return null
        }
    }
}