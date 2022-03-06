package com.honeycomb.spacedelivery.presentation.station

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honeycomb.spacedelivery.domain.common.base.BaseResult
import com.honeycomb.spacedelivery.domain.station.entity.StationEntity
import com.honeycomb.spacedelivery.domain.station.usecase.StationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StationServiceViewModel @Inject constructor(private val stationUseCase: StationUseCase) : ViewModel(){
    private val state = MutableStateFlow<StationState>(StationState.Init)
    val mState: StateFlow<StationState> get() = state

    private val stations = MutableStateFlow<List<StationEntity>>(mutableListOf())
    val mStations: StateFlow<List<StationEntity>> get() = stations

    init {
        fetchStation()
    }

    private fun setLoading(){
        state.value = StationState.IsLoading(true)
    }

    private fun hideLoading(){
        state.value = StationState.IsLoading(false)
    }

    private fun showToast(message: String){
        state.value = StationState.ShowToast(message)
    }

    fun fetchStation(){
        viewModelScope.launch {
            stationUseCase.execute()
                .onStart  {
                    setLoading()
                }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.message.toString())
                }
                .collect {
                    hideLoading()
                    when(it){
                        is BaseResult.Success -> {
                            stations.value = it.data
                        }
                        is BaseResult.Error -> {
                            showToast(it.toString())
                        }
                    }
                }
        }
    }
}

sealed class StationState {
    object Init : StationState()
    data class IsLoading(val isLoading: Boolean) : StationState()
    data class ShowToast(val message : String) : StationState()
}