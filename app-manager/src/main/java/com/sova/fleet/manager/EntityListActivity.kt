package com.sova.fleet.manager

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EntityListActivity : AppCompatActivity() {
    private lateinit var type: ManagerEntityType
    private lateinit var adapter: ManagerItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entity_list)

        type = ManagerEntityType.valueOf(
            intent.getStringExtra(EXTRA_ENTITY_TYPE) ?: ManagerEntityType.VEHICLES.name,
        )

        findViewById<TextView>(R.id.entitiesTitle).text = type.label

        adapter = ManagerItemAdapter(
            onOpen = { id -> openDetailsCard(id) },
            onEdit = { id -> openFormDialog(id) },
            onDelete = { id -> deleteEntity(id) },
        )

        findViewById<RecyclerView>(R.id.entitiesRecycler).apply {
            layoutManager = LinearLayoutManager(this@EntityListActivity)
            adapter = this@EntityListActivity.adapter
        }
        findViewById<FloatingActionButton>(R.id.fabAddEntity).setOnClickListener { openFormDialog(null) }
        render()
    }

    private fun render() {
        val list = when (type) {
            ManagerEntityType.VEHICLES -> ManagerRepository.vehicles.map {
                ManagerListItem(
                    it.id,
                    "${it.model} • ${it.vin}",
                    "Статус: ${it.status} • Парк: ${ManagerRepository.fleetName(it.fleetId)}",
                )
            }
            ManagerEntityType.FLEETS -> ManagerRepository.fleets.map {
                val vehiclesCount = ManagerRepository.vehicles.count { v -> v.fleetId == it.id }
                ManagerListItem(
                    it.id,
                    it.name,
                    "${it.region} • Компания: ${ManagerRepository.companyName(it.companyId)} • ТС: $vehiclesCount",
                )
            }
            ManagerEntityType.COMPANIES -> ManagerRepository.companies.map {
                val fleetsCount = ManagerRepository.fleets.count { f -> f.companyId == it.id }
                val usersCount = ManagerRepository.users.count { u -> u.companyId == it.id }
                ManagerListItem(it.id, it.name, "${it.city} • ${it.contact} • Парков: $fleetsCount • Пользователей: $usersCount")
            }
            ManagerEntityType.USERS -> ManagerRepository.users.map {
                ManagerListItem(
                    it.id,
                    "${it.fullName} • ${it.role}",
                    "${it.phone} • Компания: ${ManagerRepository.companyName(it.companyId)}",
                )
            }
        }
        adapter.submit(list)
    }

    private fun openDetailsCard(id: String) {
        val message = when (type) {
            ManagerEntityType.VEHICLES -> {
                val vehicle = ManagerRepository.vehicles.firstOrNull { it.id == id } ?: return
                val fleet = ManagerRepository.fleets.firstOrNull { it.id == vehicle.fleetId }
                val company = fleet?.let { f -> ManagerRepository.companies.firstOrNull { it.id == f.companyId } }
                "VIN: ${vehicle.vin}\nМодель: ${vehicle.model}\nСтатус: ${vehicle.status}\nПарк: ${fleet?.name ?: "—"}\nКомпания: ${company?.name ?: "—"}"
            }
            ManagerEntityType.FLEETS -> {
                val fleet = ManagerRepository.fleets.firstOrNull { it.id == id } ?: return
                val company = ManagerRepository.companies.firstOrNull { it.id == fleet.companyId }
                val vehicles = ManagerRepository.vehicles.filter { it.fleetId == fleet.id }
                "Парк: ${fleet.name}\nРегион: ${fleet.region}\nКомпания: ${company?.name ?: "—"}\nТС:\n${vehicles.joinToString("\n") { "- ${it.model} (${it.vin})" }}"
            }
            ManagerEntityType.COMPANIES -> {
                val company = ManagerRepository.companies.firstOrNull { it.id == id } ?: return
                val fleets = ManagerRepository.fleets.filter { it.companyId == company.id }
                val vehicles = ManagerRepository.vehicles.filter { v -> fleets.any { it.id == v.fleetId } }
                "Компания: ${company.name}\nГород: ${company.city}\nКонтакт: ${company.contact}\nПарки:\n${fleets.joinToString("\n") { "- ${it.name}" }}\nТС: ${vehicles.size}"
            }
            ManagerEntityType.USERS -> {
                val user = ManagerRepository.users.firstOrNull { it.id == id } ?: return
                val company = ManagerRepository.companies.firstOrNull { it.id == user.companyId }
                "ФИО: ${user.fullName}\nРоль: ${user.role}\nТелефон: ${user.phone}\nКомпания: ${company?.name ?: "—"}"
            }
        }
        AlertDialog.Builder(this)
            .setTitle("Карточка")
            .setMessage(message)
            .setPositiveButton("ОК", null)
            .show()
    }

    private fun openFormDialog(id: String?) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_entity_form, null)
        val inputA = view.findViewById<EditText>(R.id.inputA)
        val inputB = view.findViewById<EditText>(R.id.inputB)
        val inputC = view.findViewById<EditText>(R.id.inputC)
        val inputD = view.findViewById<EditText>(R.id.inputD)

        when (type) {
            ManagerEntityType.VEHICLES -> {
                inputA.hint = "VIN"
                inputB.hint = "Модель"
                inputC.hint = "Статус"
                inputD.hint = "ID парка (f1/f2)"
                ManagerRepository.vehicles.firstOrNull { it.id == id }?.let {
                    inputA.setText(it.vin); inputB.setText(it.model); inputC.setText(it.status); inputD.setText(it.fleetId)
                }
            }
            ManagerEntityType.FLEETS -> {
                inputA.hint = "Название"
                inputB.hint = "Регион"
                inputC.hint = "ID компании (c1/c2)"
                inputD.hint = "Примечание (необязательно)"
                ManagerRepository.fleets.firstOrNull { it.id == id }?.let {
                    inputA.setText(it.name); inputB.setText(it.region); inputC.setText(it.companyId)
                }
            }
            ManagerEntityType.COMPANIES -> {
                inputA.hint = "Компания"
                inputB.hint = "Город"
                inputC.hint = "Контакт"
                inputD.hint = "Примечание (необязательно)"
                ManagerRepository.companies.firstOrNull { it.id == id }?.let {
                    inputA.setText(it.name); inputB.setText(it.city); inputC.setText(it.contact)
                }
            }
            ManagerEntityType.USERS -> {
                inputA.hint = "ФИО"
                inputB.hint = "Роль"
                inputC.hint = "ID компании (c1/c2)"
                inputD.hint = "Телефон"
                ManagerRepository.users.firstOrNull { it.id == id }?.let {
                    inputA.setText(it.fullName); inputB.setText(it.role); inputC.setText(it.companyId); inputD.setText(it.phone)
                }
            }
        }

        AlertDialog.Builder(this)
            .setTitle(if (id == null) "Добавить" else "Редактировать")
            .setView(view)
            .setNegativeButton("Отмена", null)
            .setPositiveButton("Сохранить") { _, _ ->
                val a = inputA.text.toString().trim()
                val b = inputB.text.toString().trim()
                val c = inputC.text.toString().trim()
                val d = inputD.text.toString().trim()
                if (a.isNotEmpty() && b.isNotEmpty() && c.isNotEmpty()) {
                    upsertEntity(id, a, b, c, d)
                    render()
                }
            }
            .show()
    }

    private fun upsertEntity(id: String?, a: String, b: String, c: String, d: String) {
        when (type) {
            ManagerEntityType.VEHICLES -> {
                val existing = ManagerRepository.vehicles.firstOrNull { it.id == id }
                if (existing == null) {
                    ManagerRepository.vehicles.add(Vehicle("v${System.currentTimeMillis()}", a, b, c, if (d.isBlank()) "f1" else d))
                } else {
                    existing.vin = a; existing.model = b; existing.status = c; existing.fleetId = if (d.isBlank()) existing.fleetId else d
                }
            }
            ManagerEntityType.FLEETS -> {
                val existing = ManagerRepository.fleets.firstOrNull { it.id == id }
                if (existing == null) {
                    ManagerRepository.fleets.add(Fleet("f${System.currentTimeMillis()}", a, b, c))
                } else {
                    existing.name = a; existing.region = b; existing.companyId = c
                }
            }
            ManagerEntityType.COMPANIES -> {
                val existing = ManagerRepository.companies.firstOrNull { it.id == id }
                if (existing == null) {
                    ManagerRepository.companies.add(Company("c${System.currentTimeMillis()}", a, b, c))
                } else {
                    existing.name = a; existing.city = b; existing.contact = c
                }
            }
            ManagerEntityType.USERS -> {
                val existing = ManagerRepository.users.firstOrNull { it.id == id }
                if (existing == null) {
                    ManagerRepository.users.add(User("u${System.currentTimeMillis()}", a, b, c, d))
                } else {
                    existing.fullName = a; existing.role = b; existing.companyId = c; existing.phone = d
                }
            }
        }
    }

    private fun deleteEntity(id: String) {
        when (type) {
            ManagerEntityType.VEHICLES -> ManagerRepository.vehicles.removeAll { it.id == id }
            ManagerEntityType.FLEETS -> ManagerRepository.fleets.removeAll { it.id == id }
            ManagerEntityType.COMPANIES -> ManagerRepository.companies.removeAll { it.id == id }
            ManagerEntityType.USERS -> ManagerRepository.users.removeAll { it.id == id }
        }
        render()
    }

    companion object {
        const val EXTRA_ENTITY_TYPE = "entity_type"
    }
}
