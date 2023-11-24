package ru.netology.mymapmarkers.repository

import androidx.lifecycle.MutableLiveData
import ru.netology.mymapmarkers.dto.Marker

interface MarkerRepostitory {
    suspend fun getAll()
    suspend fun save(marker: Marker)
    suspend fun delete(id: Long)
}