package com.sova.fleet.manager

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindOpenButtons()
    }

    override fun onResume() {
        super.onResume()
        renderKpi()
    }

    private fun bindOpenButtons() {
        findViewById<MaterialButton>(R.id.btnVehicles).setOnClickListener { openList(ManagerEntityType.VEHICLES) }
        findViewById<MaterialButton>(R.id.btnFleets).setOnClickListener { openList(ManagerEntityType.FLEETS) }
        findViewById<MaterialButton>(R.id.btnCompanies).setOnClickListener { openList(ManagerEntityType.COMPANIES) }
        findViewById<MaterialButton>(R.id.btnUsers).setOnClickListener { openList(ManagerEntityType.USERS) }
    }

    private fun openList(type: ManagerEntityType) {
        startActivity(Intent(this, EntityListActivity::class.java).putExtra(EntityListActivity.EXTRA_ENTITY_TYPE, type.name))
    }

    private fun renderKpi() {
        findViewById<TextView>(R.id.kpiVehicles).text = ManagerRepository.vehicles.size.toString()
        findViewById<TextView>(R.id.kpiFleets).text = ManagerRepository.fleets.size.toString()
        findViewById<TextView>(R.id.kpiCompanies).text = ManagerRepository.companies.size.toString()
        findViewById<TextView>(R.id.kpiUsers).text = ManagerRepository.users.size.toString()
    }
}
