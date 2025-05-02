package com.example.vinilos.data.model

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("id") val id: Int,
    @SerializedName("description") val description: String,
    @SerializedName("rating") val rating: Int?
)