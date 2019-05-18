package se.ju.agileandroidproject.Models

import kotlinx.serialization.Serializable

@Serializable
class Invoice(
    id: String,
    amount: Int,
    firstName: String,
    lastName: String,
    address: String,
    personalId: String,
    issuedAt: String,
    dueDate: String,
    paid: Boolean
)