package com.example.vinilos.data.model

import com.google.gson.annotations.SerializedName

data class Musician (
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("birthDate") val birthDate: String?,
    @SerializedName("albums") val albums: List<Album>?,
    @SerializedName("performerPrizes") val performerPrizes: List<PerformerPrizes>?,
)