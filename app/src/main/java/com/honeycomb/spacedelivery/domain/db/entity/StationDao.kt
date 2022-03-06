package com.honeycomb.spacedelivery.domain.db.entity

import androidx.lifecycle.LiveData
import androidx.room.*
import com.honeycomb.spacedelivery.domain.db.StationItem

@Dao
interface StationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: StationItem)

    @Delete
    suspend fun delete(item: StationItem)

    @Query("SELECT * FROM station_item")
    fun getAllStationItems(): LiveData<List<StationItem>>
}