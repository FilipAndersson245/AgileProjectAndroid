package se.ju.agileandroidproject.Models

import kotlinx.serialization.Serializable

@Serializable
class Session(val auth: Boolean, val token: String)