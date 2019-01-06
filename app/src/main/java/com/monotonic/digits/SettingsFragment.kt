package com.monotonic.digits

import android.os.Bundle
import android.preference.PreferenceFragment



/**
 * @author Ethan
 */

class SettingsFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
    }
}