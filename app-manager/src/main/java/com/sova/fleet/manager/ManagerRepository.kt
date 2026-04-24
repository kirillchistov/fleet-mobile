package com.sova.fleet.manager

object ManagerRepository {
    val companies = mutableListOf(
        Company("c1", "ООО «Сова Моторс»", "Москва", "+7 (495) 123-45-67"),
        Company("c2", "Ozon", "Москва", "+7 (495) 232-10-00"),
        Company("c3", "Wildberries", "Москва", "8 (800) 555-00-99"),
        Company("c4", "Яндекс", "Москва", "+7 (495) 739-70-00"),
        Company("c5", "Lamoda", "Москва", "+7 (495) 363-63-93"),
        Company("c6", "КСЭ", "Москва", "+7 (495) 748-77-48"),
    )

    val fleets = mutableListOf(
        Fleet("f1", "Сова Парк Центр", "Москва", "c1"),
        Fleet("f2", "Ozon Парк Восток", "Москва", "c2"),
        Fleet("f3", "WB Парк Север", "Москва", "c3"),
        Fleet("f4", "Yandex Парк", "Москва", "c4"),
        Fleet("f5", "Lamoda Парк", "Москва", "c5"),
        Fleet("f6", "КСЭ Парк", "Москва", "c6"),
    )

    val vehicles = mutableListOf(
        Vehicle("v1", "1HGCM82633A004352", "Sova 25", "В пути", "f1"),
        Vehicle("v2", "5XYZH4AG2JG123456", "Sova 25 (5 мест)", "Сервис", "f1"),
        Vehicle("v3", "WVWZZZ5NZJW123456", "Sova 35", "В пути", "f1"),
        Vehicle("v4", "5NMJFDAE0LH123456", "Sova 35 (Ref)", "Сервис", "f1"),
        Vehicle("v5", "5UXCR6C50PLL12345", "Sova 35", "Стоянка", "f2"),
        Vehicle("v6", "WDDZF8JB1NA123456", "Sova 25 (5 мест)", "В пути", "f2"),
        Vehicle("v7", "XTA21040051234567", "Sova 25", "В пути", "f3"),
    )

    val users = mutableListOf(
        User("u1", "Алексей Смирнов", "admin", "c1", "+7 (916) 111-22-33"),
        User("u2", "Мария Козлова", "manager", "c1", "+7 (926) 444-55-66"),
        User("u3", "Иван Петров", "user", "c1", "+7 (903) 777-88-99"),
        User("u4", "Дмитрий Николаев", "user", "c1", "+7 (915) 222-33-44"),
        User("u5", "Елена Волкова", "manager", "c2", "+7 (921) 666-77-88"),
        User("u6", "Сергей Иванов", "user", "c3", "+7 (843) 333-44-55"),
    )

    val serviceTickets = mutableListOf(
        ServiceTicket("t1", "Замена масла и фильтров", "v1", "c1", "low", "completed", "2024-12-15"),
        ServiceTicket("t2", "Ремонт тормозной системы", "v2", "c1", "high", "in_progress", "2025-01-20"),
        ServiceTicket("t3", "Диагностика двигателя", "v3", "c1", "medium", "open", "2025-02-04"),
        ServiceTicket("t4", "Кузовной ремонт", "v6", "c2", "medium", "in_progress", "2025-01-25"),
    )

    val chargingSessions = mutableListOf(
        ChargingSession("ch1", "v1", "МСК-Юг #12", "120 кВт", "charging", "08:20"),
        ChargingSession("ch2", "v3", "МСК-Север #4", "90 кВт", "queued", "09:05"),
        ChargingSession("ch3", "v6", "СПБ-Центр #3", "60 кВт", "completed", "07:10"),
    )

    fun companyName(id: String): String = companies.firstOrNull { it.id == id }?.name ?: "—"
    fun fleetName(id: String): String = fleets.firstOrNull { it.id == id }?.name ?: "—"
    fun vehicleLabel(id: String): String {
        val v = vehicles.firstOrNull { it.id == id } ?: return "—"
        return "${v.model} • ${v.vin.take(8)}…"
    }
}
