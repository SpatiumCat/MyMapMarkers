package ru.netology.mymapmarkers.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.mymapmarkers.dao.MarkerDao
import ru.netology.mymapmarkers.db.AppDb
import ru.netology.mymapmarkers.dto.Marker
import ru.netology.mymapmarkers.entity.MarkerEntity

class MarkerRepositoryImpl(): MarkerRepostitory {

    private val markerDao: MarkerDao = AppDb.getInstance().markerDao()
    private val _markers = MutableLiveData<List<Marker>>()
    val markers: LiveData<List<Marker>>
        get() = _markers

    override suspend fun getAll() {
        _markers.value = markerDao.getAll().value?.map { it.toDto() }
    }

    override suspend fun save(marker: Marker) {
        markerDao.insert(MarkerEntity.fromDto(marker))
    }

    override suspend fun delete(id: Long) {
        markerDao.removeById(id)
    }
}