package com.ethshea.digits.units

import com.ethshea.digits.Precision
import com.ethshea.digits.SciNumber

/**
 * @author Ethan
 */
class PrefixUnit(abbreviation: String, name: String, factor: String) : AtomicHumanUnit(abbreviation, name, factor, mapOf(), factor) {
    companion object {
        val One = PrefixUnit("", "one", "1")
    }
}
