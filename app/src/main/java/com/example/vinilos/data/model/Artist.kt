package com.example.vinilos.data.model

import com.google.gson.annotations.SerializedName

data class Artist (
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("description") val description: String?,
    @SerializedName("creationDate") val creationDate: String?,
    @SerializedName("albums") val albums: List<Album>?,
    @SerializedName("musicians") val musicians: List<Musician>?,
    @SerializedName("performerPrizes") val performerPrizes: List<PerformerPrizes>?
)