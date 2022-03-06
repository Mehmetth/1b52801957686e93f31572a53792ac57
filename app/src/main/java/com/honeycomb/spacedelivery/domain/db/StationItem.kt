package com.honeycomb.spacedelivery.domain.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "station_item")

data class StationItem(
    @ColumnInfo(name ="name")
    val name: String,
    @ColumnInfo(name = "coordinateX")
    val coordinateX: Float,
    @ColumnInfo(name = "coordinateY")
    val coordinateY: Float,
    @ColumnInfo(name = "capacity")
    val capacity: Int,
    @ColumnInfo(name = "stock")
    val stock: Int,
    @ColumnInfo(name = "need")
    val need: Int,
    @ColumnInfo(name = "fav")
    var fav: Boolean = false,
    @ColumnInfo(name = "travel")
    val travel: Boolean = false)
{
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null
}
