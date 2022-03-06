package com.honeycomb.spacedelivery.utils

import com.honeycomb.spacedelivery.PlanetCoordinates
import kotlin.math.pow
import kotlin.math.sqrt

class CalculationHelper {
    companion object{
        fun calculateTwoPlanetDistance(a: PlanetCoordinates, b: PlanetCoordinates): Float {
            return sqrt((a.x - b.x).pow(2) + (a.y - b.y).pow(2))
        }
    }
}