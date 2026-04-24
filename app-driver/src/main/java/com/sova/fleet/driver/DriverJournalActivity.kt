package com.sova.fleet.driver

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DriverJournalActivity : AppCompatActivity() {
    private lateinit var searchInput: EditText
    private lateinit var journalCard1: LinearLayout
    private lateinit var journalCard2: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_journal)

        searchInput = findViewById(R.id.searchInput)
        journalCard1 = findViewById(R.id.journalCard1)
        journalCard2 = findViewById(R.id.journalCard2)

        findViewById<ImageButton>(R.id.backButton).setOnClickListener { finish() }
        findViewById<ImageButton>(R.id.addButton).setOnClickListener {
            startActivity(Intent(this, DriverJournalNewRecordActivity::class.java))
        }
        findViewById<com.google.android.material.button.MaterialButton>(R.id.openDetails1).setOnClickListener {
            showDetails(
                title = getString(R.string.driver_journal_item_1),
                type = getString(R.string.driver_journal_item_type_1),
                status = getString(R.string.driver_journal_status_open),
                description = getString(R.string.driver_journal_detail_description_1),
            )
        }
        findViewById<com.google.android.material.button.MaterialButton>(R.id.openDetails2).setOnClickListener {
            showDetails(
                title = getString(R.string.driver_journal_item_2),
                type = getString(R.string.driver_journal_item_type_2),
                status = getString(R.string.driver_journal_status_in_progress),
                description = getString(R.string.driver_journal_detail_description_2),
            )
        }

        searchInput.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

                override fun afterTextChanged(s: Editable?) {
                    val q = s?.toString()?.trim()?.lowercase().orEmpty()
                    applyFilter(q)
                }
            },
        )
        findViewById<LinearLayout>(R.id.navVehicles).setOnClickListener {
            startActivity(Intent(this, DriverHomeActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navCharging).setOnClickListener {
            startActivity(
                Intent(this, DriverHomeActivity::class.java)
                    .putExtra(DriverHomeActivity.EXTRA_SECTION, DriverHomeActivity.SECTION_CHARGING),
            )
            finish()
        }
        findViewById<LinearLayout>(R.id.navHelp).setOnClickListener {
            startActivity(
                Intent(this, DriverHomeActivity::class.java)
                    .putExtra(DriverHomeActivity.EXTRA_SECTION, DriverHomeActivity.SECTION_HELP),
            )
            finish()
        }
        val inactive = getColor(com.sova.fleet.core.ui.R.color.fleet_muted_fg)
        val active = getColor(com.sova.fleet.core.ui.R.color.fleet_primary)
        findViewById<ImageView>(R.id.navVehiclesIcon).setColorFilter(inactive)
        findViewById<ImageView>(R.id.navChargingIcon).setColorFilter(inactive)
        findViewById<ImageView>(R.id.navHelpIcon).setColorFilter(active)
    }

    private fun applyFilter(query: String) {
        val showAll = query.isBlank()
        journalCard1.visibility =
            visibleIf(showAll || getString(R.string.driver_journal_item_1).lowercase().contains(query))
        journalCard2.visibility =
            visibleIf(showAll || getString(R.string.driver_journal_item_2).lowercase().contains(query))
    }

    private fun visibleIf(condition: Boolean): Int = if (condition) View.VISIBLE else View.GONE

    private fun showDetails(title: String, type: String, status: String, description: String) {
        val content = LayoutInflater.from(this).inflate(R.layout.dialog_driver_journal_details, null)
        content.findViewById<TextView>(R.id.detailsTitle).text = title
        content.findViewById<TextView>(R.id.detailsType).text = getString(R.string.driver_journal_detail_type, type)
        content.findViewById<TextView>(R.id.detailsStatus).text = getString(R.string.driver_journal_detail_status, status)
        content.findViewById<TextView>(R.id.detailsDescription).text = description

        val dialog = AlertDialog.Builder(this)
            .setView(content)
            .setPositiveButton(android.R.string.ok, null)
            .create()

        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
