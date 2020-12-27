package com.monotonic.digits

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat


/**
 * @author Ethan
 */

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}