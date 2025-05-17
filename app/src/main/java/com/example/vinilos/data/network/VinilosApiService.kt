package com.example.vinilos.data.network

import com.example.vinilos.data.model.Album
import com.example.vinilos.data.model.AlbumCreate
import com.example.vinilos.data.model.Artist
import com.example.vinilos.data.model.Collector
import com.example.vinilos.data.model.CollectedAlbumAuction
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface VinilosApiService {
    @GET("albums")
    suspend fun getAlbums(): Response<List<Album>>

    @GET("albums/{id}")
    suspend fun getAlbumDetail(@Path("id") id: Int): Response<Album>

    @POST("albums")
    suspend fun createAlbum(@Body album: AlbumCreate): Response<Album>

    @GET("bands")
    suspend fun getArtists(): Response<List<Artist>>

    @GET("bands/{id}")
    suspend fun getArtistDetail(@Path("id") id: Int): Response<Artist>

    @GET("collectors")
    suspend fun getCollectors(): Response<List<Collector>>

    @GET("collectors/{id}")
    suspend fun getCollectorDetail(@Path("id") id: Int): Response<Collector>

    @GET("collectors/{id}/albums")
    suspend fun getCollectorDetailWithAlbums(@Path("id") id: Int): Response<List<CollectedAlbumAuction>>
}