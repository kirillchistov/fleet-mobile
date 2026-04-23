package com.sova.fleet.manager

import android.content.Intent
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Button
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.button.MaterialButton

class EntityListActivity : AppCompatActivity() {
    private lateinit var type: ManagerEntityType
    private var adapter: ManagerItemAdapter? = null
    private var vehicleAdapter: VehicleCardAdapter? = null
    private var filterKey: String? = null
    private var filterValue: String? = null
    private var query: String = ""
    private var statusFilter: String = "Все статусы"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entity_list)

        type = ManagerEntityType.valueOf(
            intent.getStringExtra(EXTRA_ENTITY_TYPE) ?: ManagerEntityType.VEHICLES.name,
        )
        filterKey = intent.getStringExtra(EXTRA_FILTER_KEY)
        filterValue = intent.getStringExtra(EXTRA_FILTER_VALUE)

        findViewById<TextView>(R.id.entitiesTitle).text = if (filterKey != null) "${type.label} (фильтр)" else type.label

        val recycler = findViewById<RecyclerView>(R.id.entitiesRecycler)
        recycler.layoutManager = LinearLayoutManager(this@EntityListActivity)
        setupVehicleFilters()

        if (type == ManagerEntityType.VEHICLES) {
            vehicleAdapter = VehicleCardAdapter(
                onOpen = { id -> openDetailsScreen(id) },
                onEdit = { id -> openFormDialog(id) },
                onDelete = { id -> deleteEntity(id) },
            )
            recycler.adapter = vehicleAdapter
        } else {
            adapter = ManagerItemAdapter(
                onOpen = { id -> openDetailsScreen(id) },
                onEdit = { id -> openFormDialog(id) },
                onDelete = { id -> deleteEntity(id) },
            )
            recycler.adapter = adapter
        }

        findViewById<FloatingActionButton>(R.id.fabAddEntity).setOnClickListener { openFormDialog(null) }
        findViewById<MaterialButton>(R.id.addTopButton).setOnClickListener { openFormDialog(null) }
        render()
    }

    private fun setupVehicleFilters() {
        val filterRow = findViewById<View>(R.id.vehiclesFilterRow)
        if (type != ManagerEntityType.VEHICLES) {
            filterRow.visibility = View.GONE
            return
        }
        filterRow.visibility = View.VISIBLE

        val searchInput = findViewById<EditText>(R.id.searchInput)
        val statusButton = findViewById<MaterialButton>(R.id.statusFilterButton)
        statusButton.text = statusFilter
        statusButton.setOnClickListener { openStatusFilterDialog() }

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                query = s?.toString()?.trim().orEmpty()
                render()
            }
        })
    }

    private fun openStatusFilterDialog() {
        val options = arrayOf("Все статусы", "В пути", "Стоянка", "Сервис")
        AlertDialog.Builder(this)
            .setTitle("Фильтр статуса")
            .setItems(options) { _, index ->
                statusFilter = options[index]
                findViewById<MaterialButton>(R.id.statusFilterButton).text = statusFilter
                render()
            }
            .show()
    }

    private fun render() {
        if (type == ManagerEntityType.VEHICLES) {
            vehicleAdapter?.submit(filteredVehicles())
            return
        }
        adapter?.submit(
            when (type) {
                ManagerEntityType.FLEETS -> filteredFleets().map {
                    val vehiclesCount = ManagerRepository.vehicles.count { v -> v.fleetId == it.id }
                    ManagerListItem(it.id, it.name, "${it.region} • Компания: ${ManagerRepository.companyName(it.companyId)} • ТС: $vehiclesCount")
                }
                ManagerEntityType.COMPANIES -> filteredCompanies().map {
                    val fleetsCount = ManagerRepository.fleets.count { f -> f.companyId == it.id }
                    val usersCount = ManagerRepository.users.count { u -> u.companyId == it.id }
                    ManagerListItem(it.id, it.name, "${it.city} • ${it.contact} • Парков: $fleetsCount • Пользователей: $usersCount")
                }
                ManagerEntityType.USERS -> filteredUsers().map {
                    ManagerListItem(it.id, "${it.fullName} • ${it.role}", "${it.phone} • Компания: ${ManagerRepository.companyName(it.companyId)}")
                }
                else -> emptyList()
            },
        )
    }

    private fun openDetailsScreen(id: String) {
        startActivity(
            Intent(this, EntityDetailsActivity::class.java)
                .putExtra(EntityDetailsActivity.EXTRA_ENTITY_TYPE, type.name)
                .putExtra(EntityDetailsActivity.EXTRA_ENTITY_ID, id),
        )
    }

    private fun openFormDialog(id: String?) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_entity_form, null)
        val titleView = view.findViewById<TextView>(R.id.dialogTitle)
        val inputA = view.findViewById<EditText>(R.id.inputA)
        val inputB = view.findViewById<EditText>(R.id.inputB)
        val inputC = view.findViewById<EditText>(R.id.inputC)
        val inputD = view.findViewById<EditText>(R.id.inputD)
        val cancelButton = view.findViewById<Button>(R.id.btnCancelDialog)
        val saveButton = view.findViewById<Button>(R.id.btnSaveDialog)

        titleView.text = if (id == null) getString(R.string.add_entity) else getString(R.string.edit_entity)

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

        val dialog = AlertDialog.Builder(this).setView(view).create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        cancelButton.setOnClickListener { dialog.dismiss() }
        saveButton.setOnClickListener {
            val a = inputA.text.toString().trim()
            val b = inputB.text.toString().trim()
            val c = inputC.text.toString().trim()
            val d = inputD.text.toString().trim()
            if (a.isNotEmpty() && b.isNotEmpty() && c.isNotEmpty()) {
                upsertEntity(id, a, b, c, d)
                render()
                dialog.dismiss()
            }
        }
        dialog.show()
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

    private fun filteredVehicles(): List<Vehicle> =
        when (filterKey) {
            EntityDetailsActivity.FILTER_FLEET_ID -> ManagerRepository.vehicles.filter { it.fleetId == filterValue }
            else -> ManagerRepository.vehicles
        }.filter {
            val matchesQuery = query.isBlank() || it.model.contains(query, ignoreCase = true) || it.vin.contains(query, ignoreCase = true)
            val matchesStatus = statusFilter == "Все статусы" || it.status == statusFilter
            matchesQuery && matchesStatus
        }

    private fun filteredFleets(): List<Fleet> =
        when (filterKey) {
            EntityDetailsActivity.FILTER_COMPANY_ID -> ManagerRepository.fleets.filter { it.companyId == filterValue }
            else -> ManagerRepository.fleets
        }

    private fun filteredCompanies(): List<Company> = ManagerRepository.companies

    private fun filteredUsers(): List<User> =
        when (filterKey) {
            EntityDetailsActivity.FILTER_COMPANY_ID -> ManagerRepository.users.filter { it.companyId == filterValue }
            else -> ManagerRepository.users
        }

    companion object {
        const val EXTRA_ENTITY_TYPE = "entity_type"
        const val EXTRA_FILTER_KEY = "filter_key"
        const val EXTRA_FILTER_VALUE = "filter_value"
    }
}
