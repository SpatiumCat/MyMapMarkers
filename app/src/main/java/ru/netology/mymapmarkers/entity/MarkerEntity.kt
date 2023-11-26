package ru.netology.mymapmarkers.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.mymapmarkers.dto.Marker

@Entity
data class MarkerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
) {
    fun toDto() = Marker(
        id = id,
        name = name,
        description = description,
        latitude = latitude,
        longitude = longitude,
    )

    companion object {
        fun fromDto(marker: Marker) = MarkerEntity(
            id = marker.id,
            name = marker.name,
            description = marker.description,
            latitude = marker.latitude,
            longitude = marker.longitude,
        )
    }
}

fun List<MarkerEntity>.toDto(): List<Marker> = map { it.toDto() }
fun List<Marker>.toEntity(): List<MarkerEntity> = map { MarkerEntity.fromDto(it) }
