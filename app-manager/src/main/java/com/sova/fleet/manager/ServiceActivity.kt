package com.sova.fleet.manager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ServiceActivity : AppCompatActivity() {
    private lateinit var adapter: ManagerItemAdapter
    private val fallbackTickets = listOf(
        ServiceTicket("t1", "Замена масла и фильтров", "v1", "c1", "low", "completed", "2024-12-15"),
        ServiceTicket("t2", "Ремонт тормозной системы", "v2", "c1", "high", "in_progress", "2025-01-20"),
        ServiceTicket("t3", "Диагностика двигателя", "v3", "c1", "medium", "open", "2025-02-04"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)
        Log.d(TAG, "onCreate: ServiceActivity started")

        adapter = ManagerItemAdapter(
            onOpen = { id -> openDetails(id) },
            onEdit = { id -> openDetails(id) },
            onDelete = { _ -> },
        )

        findViewById<RecyclerView>(R.id.serviceRecycler).apply {
            layoutManager = LinearLayoutManager(this@ServiceActivity)
            this.adapter = adapter
            doOnLayout {
                Log.d(TAG, "onCreate: RecyclerView measured height=$height, width=$width")
            }
        }
        Log.d(TAG, "onCreate: RecyclerView adapter attached")

        bindBottomNavigation()
        renderTickets()
    }

    private fun bindBottomNavigation() {
        findViewById<LinearLayout>(R.id.navVehicles).setOnClickListener {
            startActivity(Intent(this, EntityListActivity::class.java).putExtra(EntityListActivity.EXTRA_ENTITY_TYPE, ManagerEntityType.VEHICLES.name))
            finish()
        }
        findViewById<LinearLayout>(R.id.navUsers).setOnClickListener {
            startActivity(Intent(this, EntityListActivity::class.java).putExtra(EntityListActivity.EXTRA_ENTITY_TYPE, ManagerEntityType.USERS.name))
            finish()
        }
        findViewById<LinearLayout>(R.id.navService).setOnClickListener {
            // already on this screen
        }
    }

    private fun renderTickets() {
        val emptyState = findViewById<android.widget.TextView>(R.id.emptyStateText)
        Log.d(TAG, "renderTickets: repository serviceTickets size=${ManagerRepository.serviceTickets.size}")
        val tickets = if (ManagerRepository.serviceTickets.isNotEmpty()) {
            ManagerRepository.serviceTickets
        } else {
            fallbackTickets
        }
        Log.d(TAG, "renderTickets: data source size=${tickets.size}, usingFallback=${tickets === fallbackTickets}")
        adapter.submit(
            tickets.map {
                ManagerListItem(
                    it.id,
                    "${it.title} • ${it.priority.uppercase()}",
                    "${ManagerRepository.vehicleLabel(it.vehicleId)} • ${it.status} • до ${it.dueDate}",
                )
            },
        )
        Log.d(TAG, "renderTickets: adapter itemCount=${adapter.itemCount}")
        emptyState.visibility = if (tickets.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        Log.d(TAG, "renderTickets: emptyState visible=${emptyState.visibility == android.view.View.VISIBLE}")
    }

    private fun openDetails(id: String) {
        Log.d(TAG, "openDetails: requested id=$id")
        val t = ManagerRepository.serviceTickets.firstOrNull { it.id == id } ?: fallbackTickets.firstOrNull { it.id == id } ?: return
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.ticket_card_title))
            .setMessage(
                "Заявка: ${t.title}\n" +
                    "Авто: ${ManagerRepository.vehicleLabel(t.vehicleId)}\n" +
                    "Компания: ${ManagerRepository.companyName(t.companyId)}\n" +
                    "Приоритет: ${t.priority}\n" +
                    "Статус: ${t.status}\n" +
                    "Дедлайн: ${t.dueDate}",
            )
            .setPositiveButton("OK", null)
            .show()
    }

    companion object {
        private const val TAG = "ServiceActivityDebug"
    }
}
