package com.monotonic.digits.human

import com.monotonic.digits.evaluator.Precision
import com.monotonic.digits.evaluator.SciNumber
import com.monotonic.digits.units.DimensionBag
import com.monotonic.digits.units.NaturalUnit

/**
 * @author Ethan
 */


open class AtomicHumanUnit(val abbreviation: String, val nameResourceId: Int, val unitDerivation: String?, dimensions: DimensionBag, factor: SciNumber.Real) : NaturalUnit(dimensions, factor) {
    constructor(abbreviation: String, nameResourceId: Int, unitDerivation: String?, dimensions: DimensionBag, factor: String = "1")
            : this(abbreviation, nameResourceId, unitDerivation, dimensions, SciNumber.Real(factor, Precision.Infinite))

    override fun toString() = abbreviation + "-" + super.toString()
}