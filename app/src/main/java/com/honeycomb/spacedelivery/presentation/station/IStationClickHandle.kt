package com.honeycomb.spacedelivery.presentation.station

import com.honeycomb.spacedelivery.domain.station.entity.StationEntity

interface IStationClickHandle {
    fun favoriteClicked(list: List<StationEntity>,station: StationEntity)
    fun travelClicked(list: List<StationEntity>,station: StationEntity)
}