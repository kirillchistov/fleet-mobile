package com.sova.fleet.driver

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import androidx.appcompat.app.AppCompatActivity

class DriverJournalNewRecordActivity : AppCompatActivity() {
    private lateinit var typeInput: EditText
    private lateinit var summaryInput: EditText
    private lateinit var addRecordButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_journal_new_record)

        findViewById<ImageButton>(R.id.closeButton).setOnClickListener { finish() }
        typeInput = findViewById(R.id.typeInput)
        summaryInput = findViewById(R.id.summaryInput)
        addRecordButton = findViewById(R.id.addRecordButton)
        addRecordButton.isEnabled = false

        findViewById<LinearLayout>(R.id.addPhotoButton).setOnClickListener {
            Toast.makeText(this, R.string.driver_photo_attach_stub, Toast.LENGTH_SHORT).show()
        }
        addRecordButton.setOnClickListener {
            Toast.makeText(this, R.string.driver_record_added, Toast.LENGTH_SHORT).show()
            finish()
        }

        val watcher =
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

                override fun afterTextChanged(s: Editable?) {
                    addRecordButton.isEnabled =
                        typeInput.text.toString().trim().isNotBlank() &&
                        summaryInput.text.toString().trim().isNotBlank()
                }
            }

        typeInput.addTextChangedListener(watcher)
        summaryInput.addTextChangedListener(watcher)

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
