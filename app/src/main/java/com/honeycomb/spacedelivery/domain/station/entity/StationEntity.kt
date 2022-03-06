package com.honeycomb.spacedelivery.domain.station.entity

data class StationEntity(
    val name: String,
    val coordinateX: Float,
    val coordinateY: Float,
    val capacity: Int,
    val stock: Int,
    val need: Int,
    var fav : Boolean = false,
    var travel : Boolean = false
)
