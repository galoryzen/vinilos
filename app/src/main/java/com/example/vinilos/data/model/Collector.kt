package com.example.vinilos.data.model

import com.google.gson.annotations.SerializedName
import com.example.vinilos.data.model.Comment

data class Collector(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("telephone") val telephone: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("comments") val comments: List<Comment>?
)

data class CollectedAlbumAuction(
    @SerializedName("id") val id: Int,
    @SerializedName("price") val price: Int,
    @SerializedName("status") val status: String,
    @SerializedName("album") val album: AlbumCollector,
    @SerializedName("collector") val collector: Collector
)