package com.example.vinilos.data.network

import com.example.vinilos.data.model.Album
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface VinilosApiService {
    @GET("albums")
    suspend fun getAlbums(): Response<List<Album>>

    @GET("albums/{id}")
    suspend fun getAlbumDetail(@Path("id") id: Int): Response<Album>
}