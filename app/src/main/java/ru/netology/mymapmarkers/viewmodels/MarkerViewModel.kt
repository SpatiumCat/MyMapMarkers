package ru.netology.mymapmarkers.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.mymapmarkers.db.AppDb
import ru.netology.mymapmarkers.dto.Marker
import ru.netology.mymapmarkers.repository.MarkerRepositoryImpl
import ru.netology.mymapmarkers.repository.MarkerRepostitory

class MarkerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MarkerRepostitory = MarkerRepositoryImpl(
        AppDb.getInstance(application).markerDao()
    )
    val markerList: LiveData<List<Marker>> = repository.getAll()


    init {
        viewModelScope.launch {
            repository.save(
                listOf(
                    Marker(1L, "Marker1", "First marker", 59.935493, 30.327392),
                    Marker(2L, "Marker2", "Second Marker", 59.938185, 30.32808),
                )
            )

        }
    }
    fun save(marker: Marker) {
        viewModelScope.launch {
            repository.save(marker)
        }
    }
}