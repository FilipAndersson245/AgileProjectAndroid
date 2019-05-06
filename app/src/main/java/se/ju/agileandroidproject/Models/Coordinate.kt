package se.ju.agileandroidproject.Models

import kotlinx.serialization.Serializable

@Serializable
data class Coordinate(val lon: Double, val lat: Double) {
}