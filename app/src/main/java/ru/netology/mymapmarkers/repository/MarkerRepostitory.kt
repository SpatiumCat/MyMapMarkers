package ru.netology.mymapmarkers.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.mymapmarkers.dto.Marker

interface MarkerRepostitory {
    fun getAll(): LiveData<List<Marker>>
    suspend fun save(marker: Marker)
    suspend fun save(markers: List<Marker>)
    suspend fun delete(id: Long)
    suspend fun updateDescription(id: Long, description: String)
    suspend fun updateMarker(id: Long, name: String, description: String)
}