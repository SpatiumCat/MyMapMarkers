package ru.netology.mymapmarkers.dto

data class Marker(
    val id: Long,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
) {
}
