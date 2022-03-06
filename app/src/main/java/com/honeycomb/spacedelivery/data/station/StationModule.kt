package com.honeycomb.spacedelivery.data.station

import com.honeycomb.spacedelivery.data.common.NetworkModule
import com.honeycomb.spacedelivery.data.station.remote.api.StationApi
import com.honeycomb.spacedelivery.data.station.repository.StationRepositoryImpl
import com.honeycomb.spacedelivery.domain.station.StationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class StationModule {
    @Singleton
    @Provides
    fun provideProductApi(retrofit: Retrofit) : StationApi {
        return retrofit.create(StationApi::class.java)
    }

    @Singleton
    @Provides
    fun provideProductRepository(productApi: StationApi) : StationRepository {
        return StationRepositoryImpl(productApi)
    }
}