package com.honeycomb.spacedelivery.presentation.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.honeycomb.spacedelivery.domain.db.StationRepository

class DatabaseViewModelFactory(private val repository: StationRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DatabaseViewModel(repository) as T
    }
}