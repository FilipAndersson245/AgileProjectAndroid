package se.ju.agileandroidproject.Models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class Invoice(
    val id: Int,
    val amount: Int,
    val userId: String,
    val issueDate: String,
    val dueDate: String,
    val paid: String
) {
    @Transient
    val isPaid = when (paid) {
        "1" -> true
        else -> false
    }
}