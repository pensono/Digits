package com.ethshea.digits.units

import com.ethshea.digits.human.AtomicHumanUnit

/**
 * @author Ethan
 */
class PrefixUnit(abbreviation: String, name: String, factor: String, description: String) :
        AtomicHumanUnit(abbreviation, name, description + ", " + factor.replace('e', 'ᴇ'), mapOf(), factor) {
    companion object {
        val One = PrefixUnit("", "One", "1", "One")
    }

    override fun equals(other: Any?): Boolean = other is PrefixUnit && other.abbreviation == abbreviation && other.name == name && factor == other.factor
}
