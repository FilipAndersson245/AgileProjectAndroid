package se.ju.agileandroidproject.Models

import kotlinx.serialization.Serializable

@Serializable
data class Coordinate(val lon: Float, val lat: Float) {
}