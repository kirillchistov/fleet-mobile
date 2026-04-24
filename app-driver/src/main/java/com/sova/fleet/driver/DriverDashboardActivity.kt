package com.sova.fleet.driver

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DriverDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_dashboard)

        findViewById<Button>(R.id.btnProfile).setOnClickListener {
            startActivity(Intent(this, DriverProfileActivity::class.java))
        }
        findViewById<Button>(R.id.btnAvailableCars).setOnClickListener {
            startActivity(Intent(this, DriverAvailableCarsActivity::class.java))
        }
        findViewById<Button>(R.id.btnRoutes).setOnClickListener {
            startActivity(Intent(this, DriverHomeActivity::class.java))
        }
        findViewById<Button>(R.id.btnChargingStations).setOnClickListener {
            startActivity(Intent(this, DriverHomeActivity::class.java).putExtra(DriverHomeActivity.EXTRA_SECTION, DriverHomeActivity.SECTION_CHARGING))
        }
        findViewById<Button>(R.id.btnSupportService).setOnClickListener {
            startActivity(Intent(this, DriverHomeActivity::class.java).putExtra(DriverHomeActivity.EXTRA_SECTION, DriverHomeActivity.SECTION_HELP))
        }
    }
}
