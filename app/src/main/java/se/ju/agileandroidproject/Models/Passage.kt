package se.ju.agileandroidproject.Models

import kotlinx.serialization.*

@Serializable
class Passage(
    val id: String,
    val longitude: Float,
    val latitude: Float,
    val time: String,
    val price: Float,
    val user_personal_id_number: String,
    val gantry_id: String
) {

}