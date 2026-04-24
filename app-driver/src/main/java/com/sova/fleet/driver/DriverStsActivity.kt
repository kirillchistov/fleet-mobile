package com.sova.fleet.driver

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class DriverStsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_sts)

        findViewById<ImageButton>(R.id.backButton).setOnClickListener { finish() }
    }
}
