package com.monotonic.digits.units

import com.monotonic.digits.evaluator.Precision
import com.monotonic.digits.evaluator.SciNumber
import com.monotonic.digits.human.AtomicHumanUnit

/**
 * @author Ethan
 */
class PrefixUnit(abbreviation: String, name: String, val exponent: Int, description: String) :
        AtomicHumanUnit(abbreviation, name, "$description, 1ᴇ$exponent", mapOf(), SciNumber.Real("1e$exponent", Precision.Infinite)) {
    companion object {
        val One = PrefixUnit("", "One", 0, "One")
    }

    override operator fun times(n: Int) : PrefixUnit =
            PrefixUnit(abbreviation, name, exponent * n, unitDerivation!!) // Strings here may not make sense

    override fun equals(other: Any?): Boolean = other is PrefixUnit && other.abbreviation == abbreviation && other.name == name && factor == other.factor
}
