package com.sova.fleet.manager

data class Vehicle(
    val id: String,
    var vin: String,
    var model: String,
    var status: String,
    var fleetId: String,
)

data class Fleet(
    val id: String,
    var name: String,
    var region: String,
    var companyId: String,
)

data class Company(
    val id: String,
    var name: String,
    var city: String,
    var contact: String,
)

data class User(
    val id: String,
    var fullName: String,
    var role: String,
    var companyId: String,
    var phone: String,
)
