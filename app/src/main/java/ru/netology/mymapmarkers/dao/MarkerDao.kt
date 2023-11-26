package ru.netology.mymapmarkers.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.netology.mymapmarkers.dto.Marker
import ru.netology.mymapmarkers.entity.MarkerEntity

@Dao
interface MarkerDao {

    @Query("SELECT * FROM MarkerEntity ORDER BY name DESC")
    fun getAll(): LiveData<List<MarkerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(marker: MarkerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(markers: List<MarkerEntity>)

    @Query("UPDATE MarkerEntity SET description = :description WHERE id = :id")
    suspend fun updateDescriptionById(id: Long, description: String)

    @Query("UPDATE MarkerEntity SET name = :name WHERE id = :id")
    suspend fun updateNameById(id: Long, name: String)

    @Query("DELETE FROM MarkerEntity WHERE id = :id")
    suspend fun removeById(id: Long)

    @Query("DELETE FROM MarkerEntity")
    suspend fun clearAll()
}