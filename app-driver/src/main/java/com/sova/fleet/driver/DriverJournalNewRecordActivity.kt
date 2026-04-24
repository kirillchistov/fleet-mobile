package com.sova.fleet.driver

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class DriverJournalNewRecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_journal_new_record)

        findViewById<ImageButton>(R.id.closeButton).setOnClickListener { finish() }
    }
}
