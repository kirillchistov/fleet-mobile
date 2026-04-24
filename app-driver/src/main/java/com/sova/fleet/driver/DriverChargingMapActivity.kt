package com.sova.fleet.driver

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class DriverChargingMapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_charging_map)

        findViewById<ImageButton>(R.id.backButton).setOnClickListener { finish() }
        findViewById<MaterialButton>(R.id.filterButton).setOnClickListener {
            startActivity(Intent(this, DriverChargingFilterActivity::class.java))
        }
        bindBottomNav()
    }

    private fun bindBottomNav() {
        findViewById<LinearLayout>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, DriverDashboardActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navVehicles).setOnClickListener {
            startActivity(Intent(this, DriverHomeActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navCharging).setOnClickListener { }
        findViewById<LinearLayout>(R.id.navHelp).setOnClickListener {
            startActivity(Intent(this, DriverNotificationsActivity::class.java))
            finish()
        }

        val active = getColor(com.sova.fleet.core.ui.R.color.fleet_primary)
        val inactive = getColor(com.sova.fleet.core.ui.R.color.fleet_muted_fg)
        findViewById<ImageView>(R.id.navHomeIcon).setColorFilter(inactive)
        findViewById<ImageView>(R.id.navVehiclesIcon).setColorFilter(inactive)
        findViewById<ImageView>(R.id.navChargingIcon).setColorFilter(active)
        findViewById<ImageView>(R.id.navHelpIcon).setColorFilter(inactive)
    }
}
