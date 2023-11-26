package ru.netology.mymapmarkers.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.mymapmarkers.dao.MarkerDao
import ru.netology.mymapmarkers.db.AppDb
import ru.netology.mymapmarkers.dto.Marker
import ru.netology.mymapmarkers.entity.MarkerEntity

class MarkerRepositoryImpl(
    private val markerDao: MarkerDao
): MarkerRepostitory {



    override fun getAll() = markerDao.getAll().map { list -> list.map { it.toDto() } }

    override suspend fun save(marker: Marker) {
        markerDao.insert(MarkerEntity.fromDto(marker))
    }

    override suspend fun save(markers: List<Marker>) {
        markerDao.insert(markers.map(MarkerEntity::fromDto))
    }

    override suspend fun delete(id: Long) {
        markerDao.removeById(id)
    }
}