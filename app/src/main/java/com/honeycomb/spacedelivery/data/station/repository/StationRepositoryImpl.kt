package com.honeycomb.spacedelivery.data.station.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.honeycomb.spacedelivery.data.station.remote.api.StationApi
import com.honeycomb.spacedelivery.data.station.remote.dto.StationResponse
import com.honeycomb.spacedelivery.domain.common.base.BaseResult
import com.honeycomb.spacedelivery.domain.station.StationRepository
import com.honeycomb.spacedelivery.domain.station.entity.StationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StationRepositoryImpl @Inject constructor(private val stationApi: StationApi) :
    StationRepository {
    override suspend fun getStation(): Flow<BaseResult<List<StationEntity>, StationResponse>> {
        return flow {
            val response = stationApi.getStation()
            if (response.isSuccessful){
                val body = response.body()!!

                val products = mutableListOf<StationEntity>()
                body.forEach {
                    products.add(StationEntity(it.name,it.coordinateX,it.coordinateY,it.capacity,it.stock,it.need))
                }
                emit(BaseResult.Success(products))
            }else{
                val type = object : TypeToken<StationResponse>(){}.type
                val err = Gson().fromJson<StationResponse>(response.errorBody()!!.charStream(), type)!!
                emit(BaseResult.Error(err))
            }
        }
    }
}