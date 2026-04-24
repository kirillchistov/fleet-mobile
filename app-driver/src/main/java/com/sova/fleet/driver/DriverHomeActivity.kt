package com.sova.fleet.driver

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class DriverHomeActivity : AppCompatActivity() {
    private enum class Section { VEHICLES, CHARGING, HELP }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_home)

        findViewById<LinearLayout>(R.id.openVehicleInfoTile).setOnClickListener {
            startActivity(Intent(this, DriverVehicleDetailsActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.openLogbookTile).setOnClickListener {
            startActivity(Intent(this, DriverJournalActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, DriverDashboardActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navVehicles).setOnClickListener { showSection(Section.VEHICLES) }
        findViewById<LinearLayout>(R.id.navCharging).setOnClickListener {
            startActivity(Intent(this, DriverChargingMapActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navHelp).setOnClickListener {
            startActivity(Intent(this, DriverNotificationsActivity::class.java))
            finish()
        }

        val initial = intent.getStringExtra(EXTRA_SECTION)
        when (initial) {
            SECTION_CHARGING -> showSection(Section.CHARGING)
            SECTION_HELP -> showSection(Section.HELP)
            else -> showSection(Section.VEHICLES)
        }
    }

    private fun showSection(section: Section) {
        val title = findViewById<TextView>(R.id.contentTitle)
        val vehicles = findViewById<LinearLayout>(R.id.sectionVehicles)
        val charging = findViewById<LinearLayout>(R.id.sectionCharging)
        val help = findViewById<LinearLayout>(R.id.sectionHelp)

        val vehiclesIcon = findViewById<ImageView>(R.id.navVehiclesIcon)
        val homeIcon = findViewById<ImageView>(R.id.navHomeIcon)
        val chargingIcon = findViewById<ImageView>(R.id.navChargingIcon)
        val helpIcon = findViewById<ImageView>(R.id.navHelpIcon)

        val active = getColor(com.sova.fleet.core.ui.R.color.fleet_primary)
        val inactive = getColor(com.sova.fleet.core.ui.R.color.fleet_muted_fg)

        vehicles.visibility = if (section == Section.VEHICLES) LinearLayout.VISIBLE else LinearLayout.GONE
        charging.visibility = if (section == Section.CHARGING) LinearLayout.VISIBLE else LinearLayout.GONE
        help.visibility = if (section == Section.HELP) LinearLayout.VISIBLE else LinearLayout.GONE

        homeIcon.setColorFilter(inactive)
        vehiclesIcon.setColorFilter(if (section == Section.VEHICLES) active else inactive)
        chargingIcon.setColorFilter(if (section == Section.CHARGING) active else inactive)
        helpIcon.setColorFilter(if (section == Section.HELP) active else inactive)

        title.text = when (section) {
            Section.VEHICLES -> getString(R.string.driver_auto_title)
            Section.CHARGING -> getString(R.string.bottom_nav_driver_charging)
            Section.HELP -> getString(R.string.bottom_nav_driver_help)
        }
    }

    companion object {
        const val EXTRA_SECTION = "section"
        const val SECTION_CHARGING = "charging"
        const val SECTION_HELP = "help"
    }
}
