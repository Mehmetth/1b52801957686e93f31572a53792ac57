package com.honeycomb.spacedelivery.presentation.station.viewmodel

import androidx.lifecycle.ViewModel

class StationViewModel : ViewModel()  {
    var damageCapacity = 100
    var currentPosition = 0
    var travelControl = 0

    var timerFromMillis: Long = 0
    var timerPauseState : Boolean = false
    var timerStopState : Boolean = false

    fun reduceDamageCapacity() : Int {
        damageCapacity -= 10
        return damageCapacity
    }
    fun reduceTravelCapacity() : Int {
        travelControl -= 1
        return travelControl
    }
}