package com.ethshea.digits.units

import com.ethshea.digits.SciNumber

/**
 * @author Ethan
 */


open class AtomicHumanUnit(val abbreviation: String, val name: String, dimensions: Map<String, Int>, factor: SciNumber = SciNumber.One) : NaturalUnit(dimensions, factor) {
    override fun toString() = abbreviation + " " + super.toString()
}