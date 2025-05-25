package com.example.vinilos.data.model

enum class Genre(private val displayName: String) {
    CLASSICAL("Classical"),
    SALSA("Salsa"),
    ROCK("Rock"),
    FOLK("Folk");

    override fun toString(): String {
        return displayName
    }
}