package com.example.vinilos.data.model

import com.google.gson.annotations.SerializedName

data class Performer(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String?
)