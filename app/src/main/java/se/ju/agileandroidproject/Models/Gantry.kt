package se.ju.agileandroidproject.Models

import kotlinx.serialization.*
import kotlinx.serialization.json.Json

import se.ju.agileandroidproject.Models.Coordinate

@Serializable
class Gantry(val id: String, val Coordinates: Coordinate, val lastUpdated: String, val Price: Float) {

}