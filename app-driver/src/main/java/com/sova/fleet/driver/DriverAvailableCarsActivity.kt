package com.sova.fleet.driver

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class DriverAvailableCarsActivity : AppCompatActivity() {
    private lateinit var searchInput: EditText
    private lateinit var cardSova25: LinearLayout
    private lateinit var cardSova35: LinearLayout
    private lateinit var cardEvmPro: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_available_cars)

        findViewById<ImageButton>(R.id.backButton).setOnClickListener { finish() }
        searchInput = findViewById(R.id.searchInput)
        cardSova25 = findViewById(R.id.cardSova25)
        cardSova35 = findViewById(R.id.cardSova35)
        cardEvmPro = findViewById(R.id.cardEvmPro)

        cardSova25.setOnClickListener {
            startActivity(Intent(this, DriverVehicleDetailsActivity::class.java))
        }
        cardSova35.setOnClickListener {
            startActivity(Intent(this, DriverVehicleDetailsActivity::class.java))
        }
        cardEvmPro.setOnClickListener {
            startActivity(Intent(this, DriverVehicleDetailsActivity::class.java))
        }
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
        val active = getColor(com.sova.fleet.core.ui.R.color.fleet_primary)
        val inactive = getColor(com.sova.fleet.core.ui.R.color.fleet_muted_fg)
        findViewById<ImageView>(R.id.navHomeIcon).setColorFilter(inactive)
        findViewById<ImageView>(R.id.navVehiclesIcon).setColorFilter(active)
        findViewById<ImageView>(R.id.navChargingIcon).setColorFilter(inactive)
        findViewById<ImageView>(R.id.navHelpIcon).setColorFilter(inactive)
    }

    private fun applyFilter(query: String) {
        val showAll = query.isBlank()
        cardSova25.visibility = visibleIf(showAll || "sova 25 x349mt116".contains(query))
        cardSova35.visibility = visibleIf(showAll || "sova 35 к728рх116".lowercase().contains(query))
        cardEvmPro.visibility = visibleIf(showAll || "evm pro а920нк116".lowercase().contains(query))
    }

    private fun visibleIf(condition: Boolean): Int = if (condition) View.VISIBLE else View.GONE
}
