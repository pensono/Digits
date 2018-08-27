package com.ethshea.digits

import com.ethshea.digits.evaluator.Quantity
import com.ethshea.digits.units.NaturalUnit
import com.ethshea.digits.units.UnitSystem

/**
 * @author Ethan
 */


fun q(value: String, unit: NaturalUnit) = Quantity(SciNumber(value), unit)
fun u(abbr: String) = UnitSystem.unitAbbreviations[abbr]!!
fun p(abbr: String) = UnitSystem.prefixAbbreviations[abbr]!!