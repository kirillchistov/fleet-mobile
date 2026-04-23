package com.sova.fleet.manager

object ManagerRepository {
    val companies = mutableListOf(
        Company("c1", "Sova Logistics", "Москва", "+7 999 111-22-33"),
        Company("c2", "Volt Cargo", "Санкт-Петербург", "+7 999 555-44-33"),
    )

    val fleets = mutableListOf(
        Fleet("f1", "Северный парк", "Москва", "c1"),
        Fleet("f2", "Южный парк", "Краснодар", "c2"),
    )

    val vehicles = mutableListOf(
        Vehicle("v1", "VIN001", "Volvo E-Truck", "В пути", "f1"),
        Vehicle("v2", "VIN002", "MAN eTGX", "Стоянка", "f2"),
    )

    val users = mutableListOf(
        User("u1", "Иван Петров", "Manager", "c1", "+7 999 101-20-30"),
        User("u2", "Елена Смирнова", "Driver", "c2", "+7 999 303-40-50"),
    )

    fun companyName(id: String): String = companies.firstOrNull { it.id == id }?.name ?: "—"
    fun fleetName(id: String): String = fleets.firstOrNull { it.id == id }?.name ?: "—"
}
