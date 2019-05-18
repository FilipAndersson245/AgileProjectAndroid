package se.ju.agileandroidproject.Models

import kotlinx.serialization.Serializable

@Serializable
class Invoice(
    val id: String,
    val amount: Int,
    val firstName: String,
    val lastName: String,
    val address: String,
    val personalId: String,
    val issuedAt: String,
    val dueDate: String,
    val paid: Boolean
)