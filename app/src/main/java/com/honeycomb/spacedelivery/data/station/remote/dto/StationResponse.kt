package com.honeycomb.spacedelivery.data.station.remote.dto

import com.google.gson.annotations.SerializedName

data class StationResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("coordinateX")
    val coordinateX: Float,
    @SerializedName("coordinateY")
    val coordinateY: Float,
    @SerializedName("capacity")
    val capacity: Int,
    @SerializedName("stock")
    val stock: Int,
    @SerializedName("need")
    val need: Int
    )