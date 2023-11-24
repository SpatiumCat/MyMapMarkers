package ru.netology.mymapmarkers.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.mymapmarkers.dto.Marker
import ru.netology.mymapmarkers.repository.MarkerRepostitory

class MarkerViewModel(): ViewModel() {

    private val repostitory: MarkerRepostitory = MarkerRepostitoryImpl()
    private var _markerList =
    val markerList:LiveData<List<Marker>> = _markerList

    init {
        _markerList.value = listOf(
            Marker(1L,"Marker1", "First marker", 59.935493, 30.327392),
            Marker(2L,"Marker2", "Second Marker", 59.938185, 30.32808),
        )
    }
}