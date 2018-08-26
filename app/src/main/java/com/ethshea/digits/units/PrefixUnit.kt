package com.ethshea.digits.units

import com.ethshea.digits.SciNumber

/**
 * @author Ethan
 */
class PrefixUnit(abbreviation: String, name: String, factor: SciNumber) : AtomicHumanUnit(abbreviation, name, mapOf(), factor) {
    companion object {
        val One = PrefixUnit("", "one", SciNumber.One)
    }
}
