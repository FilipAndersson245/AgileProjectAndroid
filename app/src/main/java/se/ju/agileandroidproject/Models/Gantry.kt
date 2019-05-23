package se.ju.agileandroidproject.Models

import kotlinx.serialization.*

@Serializable
class Gantry(
    val id: String,
    val longitude: Float,
    val latitude: Float,
    val lastUpdate: String,
    val price: Float
) {

}
