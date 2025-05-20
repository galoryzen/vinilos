package com.example.vinilos.data.repository

import android.util.Log
import com.example.vinilos.data.model.Track
import com.example.vinilos.data.model.TrackCreate
import com.example.vinilos.data.network.RetrofitInstance

class TrackRepository {

    private val apiService = RetrofitInstance.api

    suspend fun createTrack(albumID: Int, track: TrackCreate): Track? {
        try {
            val response = apiService.createTrack(albumID, track)
            if (response.isSuccessful) {
                Log.d("TrackRepository", "Data received: ${response.body()}")
                return response.body()
            } else {
                Log.e("TrackRepository", "API Error Response: ${response.errorBody()?.string()}")
                return null
            }
        } catch (e: Exception) {
            Log.e("TrackRepository", "Network Exception: ${e.message}", e)
            return null
        }
    }
}