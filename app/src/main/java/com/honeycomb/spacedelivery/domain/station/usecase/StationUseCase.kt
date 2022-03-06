package com.honeycomb.spacedelivery.domain.station.usecase

import com.honeycomb.spacedelivery.data.station.remote.dto.StationResponse
import com.honeycomb.spacedelivery.domain.common.base.BaseResult
import com.honeycomb.spacedelivery.domain.station.StationRepository
import com.honeycomb.spacedelivery.domain.station.entity.StationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StationUseCase @Inject constructor(private val stationRepository: StationRepository) {
    suspend fun execute(): Flow<BaseResult<List<StationEntity>, StationResponse>> {
        return stationRepository.getStation()
    }

}