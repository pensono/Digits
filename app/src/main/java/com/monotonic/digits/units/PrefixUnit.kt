package com.monotonic.digits.units

import com.monotonic.digits.evaluator.Precision
import com.monotonic.digits.evaluator.SciNumber
import com.monotonic.digits.human.AtomicHumanUnit

/**
 * @author Ethan
 */
class PrefixUnit(abbreviation: String, name: String, factor: SciNumber.Real, derivation: String) :
        AtomicHumanUnit(abbreviation, name, derivation, mapOf(), factor) {
    companion object {
        val One = PrefixUnit("", "One", "1", "One")
    }

    constructor(abbreviation: String, name: String, factor: String, description: String) :
        this(abbreviation, name, SciNumber.Real(factor, Precision.Infinite), description + ", " + factor.replace('e', 'ᴇ'))


    override operator fun times(n: Int) : PrefixUnit =
            PrefixUnit(abbreviation, name, factor.pow(n), unitDerivation!!) // Strings here may not make sense

    override fun equals(other: Any?): Boolean = other is PrefixUnit && other.abbreviation == abbreviation && other.name == name && factor == other.factor
}
