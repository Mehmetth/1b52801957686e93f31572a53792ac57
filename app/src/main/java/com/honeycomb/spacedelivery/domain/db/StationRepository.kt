package com.honeycomb.spacedelivery.domain.db

class StationRepository(private val db: StationDatabase) {

    suspend fun insert(item:StationItem) = db.getGroceryDao().insert(item)
    suspend fun delete(item:StationItem) = db.getGroceryDao().delete(item)

    fun allStationItems() = db.getGroceryDao().getAllStationItems()
}