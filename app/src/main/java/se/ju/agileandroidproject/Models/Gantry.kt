package se.ju.agileandroidproject.Models

import kotlinx.serialization.*

@Serializable
class Gantry(
    val id: String,
    val position: List<Float>,
    val lastUpdated: String,
    val price: Float) {

    @Transient
    val coordinates = Coordinate(position[0], position[1])

}
