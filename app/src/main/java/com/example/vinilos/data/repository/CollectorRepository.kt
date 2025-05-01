package com.example.vinilos.data.repository

import android.util.Log
import com.example.vinilos.data.model.Collector
import com.example.vinilos.data.network.RetrofitInstance

class CollectorRepository {

    private val apiService = RetrofitInstance.api

    suspend fun getAllCollectors(): List<Collector>? {
        try {
            val response = apiService.getCollectors()
            if (response.isSuccessful) {
                Log.d("CollectorRepository", "Data received: ${response.body()}")
                return response.body()
            } else {
                Log.e("CollectorRepository", "API Error Response: ${response.errorBody()?.string()}")
                return null
            }
        } catch (e: Exception) {
            Log.e("CollectorRepository", "Network Exception: ${e.message}", e)
            return null
        }
    }
}