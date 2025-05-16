package com.example.vinilos.data.model

enum class RecordLabel(private val displayName: String) {
    SONY("Sony Music"),
    EMI("EMI"),
    FUENTES("Discos Fuentes"),
    ELEKTRA("Elektra"),
    FANIA("Fania Records");

    override fun toString(): String {
        return displayName
    }
}
