package com.sova.fleet.driver

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class DriverVehicleDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_vehicle_details)

        findViewById<ImageButton>(R.id.backButton).setOnClickListener { finish() }
        findViewById<LinearLayout>(R.id.stsCard).setOnClickListener {
            startActivity(Intent(this, DriverStsActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.specsRow).setOnClickListener {
            startActivity(Intent(this, DriverSpecsActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.navVehicles).setOnClickListener { finish() }
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

        val active = getColor(com.sova.fleet.core.ui.R.color.fleet_primary)
        val inactive = getColor(com.sova.fleet.core.ui.R.color.fleet_muted_fg)
        findViewById<ImageView>(R.id.navVehiclesIcon).setColorFilter(active)
        findViewById<ImageView>(R.id.navChargingIcon).setColorFilter(inactive)
        findViewById<ImageView>(R.id.navHelpIcon).setColorFilter(inactive)
    }
}
