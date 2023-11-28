package ru.netology.mymapmarkers.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.mymapmarkers.db.AppDb
import ru.netology.mymapmarkers.dto.Marker
import ru.netology.mymapmarkers.repository.MarkerRepositoryImpl
import ru.netology.mymapmarkers.repository.MarkerRepostitory
private val emptyMarker = Marker(
    id = 0L,
    name = "",
    description = "",
    latitude = 0.0,
    longitude = 0.0,
)
class MarkerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MarkerRepostitory = MarkerRepositoryImpl(
        AppDb.getInstance(application).markerDao()
    )
    val markerList: LiveData<List<Marker>> = repository.getAll()

    private val edited = MutableLiveData(emptyMarker)

    init {

    }
    fun save(marker: Marker) {
        viewModelScope.launch {
            repository.save(marker)
        }
    }
    fun removeById(id: Long) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }

    fun updateDescription(id: Long, description: String){
        viewModelScope.launch {
            repository.updateDescription(id, description)
        }
    }

    fun updateMarker(id: Long, name: String, description: String) {
        viewModelScope.launch {
            repository.updateMarker(id, name, description)
        }
    }

    fun edit(marker: Marker){
        edited.value = marker
    }
}