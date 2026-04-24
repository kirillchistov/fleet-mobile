package com.sova.fleet.driver

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class DriverEditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_edit_profile)

        findViewById<ImageButton>(R.id.backButton).setOnClickListener { finish() }
        findViewById<MaterialButton>(R.id.saveButton).setOnClickListener {
            Toast.makeText(this, R.string.driver_profile_saved, Toast.LENGTH_SHORT).show()
            finish()
        }
        bindBottomNav(activeHome = false, activeVehicles = false, activeHelp = true)
    }

    private fun bindBottomNav(activeHome: Boolean, activeVehicles: Boolean, activeHelp: Boolean) {
        findViewById<LinearLayout>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, DriverDashboardActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navVehicles).setOnClickListener {
            startActivity(Intent(this, DriverHomeActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navCharging).setOnClickListener {
            startActivity(Intent(this, DriverChargingMapActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navHelp).setOnClickListener {
            startActivity(Intent(this, DriverNotificationsActivity::class.java))
            finish()
        }

        val active = getColor(com.sova.fleet.core.ui.R.color.fleet_primary)
        val inactive = getColor(com.sova.fleet.core.ui.R.color.fleet_muted_fg)
        findViewById<ImageView>(R.id.navHomeIcon).setColorFilter(if (activeHome) active else inactive)
        findViewById<ImageView>(R.id.navVehiclesIcon).setColorFilter(if (activeVehicles) active else inactive)
        findViewById<ImageView>(R.id.navChargingIcon).setColorFilter(inactive)
        findViewById<ImageView>(R.id.navHelpIcon).setColorFilter(if (activeHelp) active else inactive)
    }
}
