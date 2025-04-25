package com.example.vinilos.data.model

import com.google.gson.annotations.SerializedName

data class PerformerPrizes (
    @SerializedName("id") val id: Int,
    @SerializedName("premiationDate") val premiationDate: String?,
)
