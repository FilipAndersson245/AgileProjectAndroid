package se.ju.agileandroidproject.Models

import kotlinx.serialization.Serializable

@Serializable
class User(
    val username: String,
    val personalIdNumber: String,
    val password: String,
    val email: String,
    val address: String,
    val firstName: String,
    val lastName: String
)