package com.example.vinilos.data.network

import com.example.vinilos.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = BuildConfig.BASE_URL

    val api: VinilosApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VinilosApiService::class.java)
    }
}