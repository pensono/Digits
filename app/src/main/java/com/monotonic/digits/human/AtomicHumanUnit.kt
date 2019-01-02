package com.monotonic.digits.human

import com.monotonic.digits.evaluator.Precision
import com.monotonic.digits.evaluator.SciNumber
import com.monotonic.digits.units.NaturalUnit

/**
 * @author Ethan
 */


open class AtomicHumanUnit(val abbreviation: String, val name: String, val unitDerivation: String?, dimensions: Map<String, Int>, factor: SciNumber.Real) : NaturalUnit(dimensions, factor) {
    constructor(abbreviation: String, name: String, unitDerivation: String?, dimensions: Map<String, Int>, factor: String = "1")
            : this(abbreviation, name, unitDerivation, dimensions, SciNumber.Real(factor, Precision.Infinite))
    override fun toString() = abbreviation + " " + super.toString()

//    override operator fun times(amt: Int) = HumanUnit(mapOf(this to amt))
}