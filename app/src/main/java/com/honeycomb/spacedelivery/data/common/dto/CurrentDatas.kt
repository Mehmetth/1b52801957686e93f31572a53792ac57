package com.honeycomb.spacedelivery.data.common.dto

data class CurrentDatas(
    var name: String,
    var coordinateX: Float,
    var coordinateY: Float,
    var ugs: Float,
    var eus: Float,
    var damageCapacity: Int,
    var traveledStations: Int = 0
)