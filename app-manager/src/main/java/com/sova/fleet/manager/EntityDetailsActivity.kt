package com.sova.fleet.manager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class EntityDetailsActivity : AppCompatActivity() {
    private lateinit var type: ManagerEntityType
    private lateinit var entityId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entity_details)
        findViewById<ImageButton>(R.id.backButton).setOnClickListener { finish() }

        type = ManagerEntityType.valueOf(intent.getStringExtra(EXTRA_ENTITY_TYPE) ?: ManagerEntityType.VEHICLES.name)
        entityId = intent.getStringExtra(EXTRA_ENTITY_ID).orEmpty()

        render()
    }

    private fun render() {
        val title = findViewById<TextView>(R.id.detailsTitle)
        val body = findViewById<TextView>(R.id.detailsBody)
        val relationPrimary = findViewById<Button>(R.id.relationPrimary)
        val relationSecondary = findViewById<Button>(R.id.relationSecondary)
        val vehicleImage = findViewById<ImageView>(R.id.vehicleImage)
        val detailsMenuTitle = findViewById<TextView>(R.id.detailsMenuTitle)
        val documentsRow = findViewById<View>(R.id.documentsRow)
        val menuRows = findViewById<View>(R.id.menuRows)
        val headerTitle = findViewById<TextView>(R.id.detailsHeaderTitle)

        relationPrimary.visibility = Button.GONE
        relationSecondary.visibility = Button.GONE
        vehicleImage.visibility = View.GONE
        detailsMenuTitle.visibility = View.GONE
        documentsRow.visibility = View.GONE
        menuRows.visibility = View.GONE

        when (type) {
            ManagerEntityType.VEHICLES -> {
                val vehicle = ManagerRepository.vehicles.firstOrNull { it.id == entityId } ?: return
                val fleet = ManagerRepository.fleets.firstOrNull { it.id == vehicle.fleetId }
                val company = fleet?.let { f -> ManagerRepository.companies.firstOrNull { it.id == f.companyId } }
                headerTitle.text = getString(R.string.vehicle_info_title)
                title.text = vehicle.model
                body.text = "Госномер: X349MT116\nVIN: ${vehicle.vin}\nСтатус: ${vehicle.status}\nПарк: ${fleet?.name ?: "—"}\nКомпания: ${company?.name ?: "—"}"
                vehicleImage.visibility = View.VISIBLE
                detailsMenuTitle.visibility = View.VISIBLE
                documentsRow.visibility = View.VISIBLE
                menuRows.visibility = View.VISIBLE

                relationPrimary.visibility = Button.VISIBLE
                relationPrimary.text = "Открыть парк"
                relationPrimary.setOnClickListener {
                    fleet?.let { openDetails(ManagerEntityType.FLEETS, it.id) }
                }
            }
            ManagerEntityType.FLEETS -> {
                val fleet = ManagerRepository.fleets.firstOrNull { it.id == entityId } ?: return
                val company = ManagerRepository.companies.firstOrNull { it.id == fleet.companyId }
                val vehicles = ManagerRepository.vehicles.count { it.fleetId == fleet.id }
                headerTitle.text = getString(R.string.entity_details_title)
                title.text = fleet.name
                body.text = "Регион: ${fleet.region}\nКомпания: ${company?.name ?: "—"}\nКоличество ТС: $vehicles"

                relationPrimary.visibility = Button.VISIBLE
                relationPrimary.text = "Список автомобилей парка"
                relationPrimary.setOnClickListener {
                    openFilteredList(ManagerEntityType.VEHICLES, FILTER_FLEET_ID, fleet.id)
                }

                relationSecondary.visibility = Button.VISIBLE
                relationSecondary.text = "Открыть компанию"
                relationSecondary.setOnClickListener {
                    openDetails(ManagerEntityType.COMPANIES, fleet.companyId)
                }
            }
            ManagerEntityType.COMPANIES -> {
                val company = ManagerRepository.companies.firstOrNull { it.id == entityId } ?: return
                val fleets = ManagerRepository.fleets.count { it.companyId == company.id }
                val users = ManagerRepository.users.count { it.companyId == company.id }
                headerTitle.text = getString(R.string.entity_details_title)
                title.text = company.name
                body.text = "Город: ${company.city}\nКонтакт: ${company.contact}\nПарков: $fleets\nПользователей: $users"

                relationPrimary.visibility = Button.VISIBLE
                relationPrimary.text = "Список автопарков компании"
                relationPrimary.setOnClickListener {
                    openFilteredList(ManagerEntityType.FLEETS, FILTER_COMPANY_ID, company.id)
                }

                relationSecondary.visibility = Button.VISIBLE
                relationSecondary.text = "Список пользователей компании"
                relationSecondary.setOnClickListener {
                    openFilteredList(ManagerEntityType.USERS, FILTER_COMPANY_ID, company.id)
                }
            }
            ManagerEntityType.USERS -> {
                val user = ManagerRepository.users.firstOrNull { it.id == entityId } ?: return
                val company = ManagerRepository.companies.firstOrNull { it.id == user.companyId }
                headerTitle.text = getString(R.string.entity_details_title)
                title.text = user.fullName
                body.text = "Роль: ${user.role}\nТелефон: ${user.phone}\nКомпания: ${company?.name ?: "—"}"

                relationPrimary.visibility = Button.VISIBLE
                relationPrimary.text = "Открыть компанию"
                relationPrimary.setOnClickListener {
                    openDetails(ManagerEntityType.COMPANIES, user.companyId)
                }
            }
        }
    }

    private fun openDetails(type: ManagerEntityType, id: String) {
        startActivity(
            Intent(this, EntityDetailsActivity::class.java)
                .putExtra(EXTRA_ENTITY_TYPE, type.name)
                .putExtra(EXTRA_ENTITY_ID, id),
        )
    }

    private fun openFilteredList(type: ManagerEntityType, filterKey: String, filterValue: String) {
        startActivity(
            Intent(this, EntityListActivity::class.java)
                .putExtra(EntityListActivity.EXTRA_ENTITY_TYPE, type.name)
                .putExtra(EntityListActivity.EXTRA_FILTER_KEY, filterKey)
                .putExtra(EntityListActivity.EXTRA_FILTER_VALUE, filterValue),
        )
    }

    companion object {
        const val EXTRA_ENTITY_TYPE = "entity_type"
        const val EXTRA_ENTITY_ID = "entity_id"
        const val FILTER_COMPANY_ID = "company_id"
        const val FILTER_FLEET_ID = "fleet_id"
    }
}
