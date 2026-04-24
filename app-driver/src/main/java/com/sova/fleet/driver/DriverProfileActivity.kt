package com.sova.fleet.driver

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import androidx.appcompat.app.AppCompatActivity

class DriverProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_profile)

        findViewById<ImageButton>(R.id.backButton).setOnClickListener { finish() }
        findViewById<MaterialButton>(R.id.editButton).setOnClickListener {
            Toast.makeText(this, R.string.edit_in_progress, Toast.LENGTH_SHORT).show()
        }
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
}
