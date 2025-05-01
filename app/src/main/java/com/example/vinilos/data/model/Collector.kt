package com.example.vinilos.data.model

import com.google.gson.annotations.SerializedName

data class Collector(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("telephone") val telephone: String?,
    @SerializedName("email") val email: String?
)