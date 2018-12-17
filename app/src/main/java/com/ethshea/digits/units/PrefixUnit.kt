package com.ethshea.digits.units

import com.ethshea.digits.Precision
import com.ethshea.digits.SciNumber

/**
 * @author Ethan
 */
class PrefixUnit(abbreviation: String, name: String, factor: SciNumber) : AtomicHumanUnit(abbreviation, name, mapOf(), factor) {
    constructor(abbreviation: String, name: String, factor: String) : this(abbreviation, name, SciNumber(factor, Precision.Infinite))
    companion object {
        val One = PrefixUnit("", "one", "1")
    }
}
