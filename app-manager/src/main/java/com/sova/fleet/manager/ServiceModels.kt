package com.sova.fleet.manager

data class ServiceTicket(
    val id: String,
    val title: String,
    val vehicleId: String,
    val companyId: String,
    val priority: String,
    val status: String,
    val dueDate: String,
)

data class ChargingSession(
    val id: String,
    val vehicleId: String,
    val station: String,
    val power: String,
    val status: String,
    val startedAt: String,
)
