package com.ethshea.digits.units

import com.ethshea.digits.Precision
import com.ethshea.digits.SciNumber

/**
 * @author Ethan
 */


open class AtomicHumanUnit(val abbreviation: String, val name: String, dimensions: Map<String, Int>, factor: SciNumber) : NaturalUnit(dimensions, factor) {
    constructor(abbreviation: String, name: String, dimensions: Map<String, Int>, factor: String = "1") : this(abbreviation, name, dimensions, SciNumber(factor, Precision.Infinite))
    override fun toString() = abbreviation + " " + super.toString()
}