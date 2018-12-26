package com.ethshea.digits.units

import com.ethshea.digits.human.AtomicHumanUnit

/**
 * @author Ethan
 */
class PrefixUnit(abbreviation: String, name: String, factor: String) : AtomicHumanUnit(abbreviation, name, factor, mapOf(), factor) {
    companion object {
        val One = PrefixUnit("", "one", "1")
    }
}
