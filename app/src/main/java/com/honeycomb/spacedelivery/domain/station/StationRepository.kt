package com.honeycomb.spacedelivery.domain.station

import com.honeycomb.spacedelivery.data.station.remote.dto.StationResponse
import com.honeycomb.spacedelivery.domain.common.base.BaseResult
import com.honeycomb.spacedelivery.domain.station.entity.StationEntity
import kotlinx.coroutines.flow.Flow

interface StationRepository{
    suspend fun getStation() : Flow<BaseResult<List<StationEntity>, StationResponse>>
}