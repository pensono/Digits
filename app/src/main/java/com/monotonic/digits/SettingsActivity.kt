package com.monotonic.digits

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * @author Ethan
 */
class SettingsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.setDisplayHomeAsUpEnabled(true) // Show back button
        fragmentManager
                .beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .commit()
    }
}

