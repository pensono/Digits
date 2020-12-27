package com.monotonic.digits

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * For some reason, the official settings document uses appcompat. https://developer.android.com/guide/topics/ui/settings/
 * @author Ethan
 */
class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.setDisplayHomeAsUpEnabled(true) // Show back button
        supportFragmentManager
                .beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .commit()
    }
}

