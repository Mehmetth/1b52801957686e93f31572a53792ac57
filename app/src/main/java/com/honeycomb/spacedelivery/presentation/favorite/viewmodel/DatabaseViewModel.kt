package com.honeycomb.spacedelivery.presentation.favorite.viewmodel

import androidx.lifecycle.ViewModel
import com.honeycomb.spacedelivery.domain.db.StationItem
import com.honeycomb.spacedelivery.domain.db.StationRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DatabaseViewModel(private val repository: StationRepository): ViewModel() {

    fun insert(item:StationItem) = GlobalScope.launch {
        repository.insert(item)
    }
    fun delete(item: StationItem) = GlobalScope.launch {
        repository.delete(item)
    }
    fun allStationItems() = repository.allStationItems()
}