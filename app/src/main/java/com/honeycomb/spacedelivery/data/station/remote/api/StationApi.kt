package com.honeycomb.spacedelivery.data.station.remote.api

import com.honeycomb.spacedelivery.data.station.remote.dto.StationResponse
import retrofit2.Response
import retrofit2.http.GET

interface StationApi {
    @GET("e7211664-cbb6-4357-9c9d-f12bf8bab2e2")
    suspend fun getStation() : Response<List<StationResponse>>
}