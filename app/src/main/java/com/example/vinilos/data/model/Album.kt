package com.example.vinilos.data.model

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("cover") val cover: String,

    @SerializedName("performers") val performers: List<Performer>?,

    @SerializedName("releaseDate") val releaseDate: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("genre") val genre: String?,
    @SerializedName("recordLabel") val recordLabel: String?
)