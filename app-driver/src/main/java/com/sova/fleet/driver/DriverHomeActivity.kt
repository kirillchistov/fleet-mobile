package com.sova.fleet.driver

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class DriverHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_home)

        val contentTitle = findViewById<TextView>(R.id.contentTitle)
        findViewById<TextView>(R.id.navVehicles).setOnClickListener { contentTitle.text = getString(R.string.bottom_nav_vehicles) }
        findViewById<TextView>(R.id.navCharging).setOnClickListener { contentTitle.text = getString(R.string.bottom_nav_driver_charging) }
        findViewById<TextView>(R.id.navHelp).setOnClickListener { contentTitle.text = getString(R.string.bottom_nav_driver_help) }
    }
}
