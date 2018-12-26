package com.ethshea.digits

import com.ethshea.digits.evaluator.Precision
import com.ethshea.digits.evaluator.Quantity
import com.ethshea.digits.evaluator.SciNumber
import com.ethshea.digits.units.NaturalUnit
import com.ethshea.digits.units.UnitSystem

/**
 * @author Ethan
 */


fun q(value: String, unit: NaturalUnit) = Quantity(SciNumber(value), unit)
fun u(abbr: String) = UnitSystem.unitAbbreviations[abbr]!!
fun p(abbr: String) = UnitSystem.prefixAbbreviations[abbr]!!
fun sf(i: Int) = Precision.SigFigs(i)

val Kilo = SciNumber("1e3", Precision.Infinite)
val Milli = SciNumber("1e-3", Precision.Infinite)
val Mega = SciNumber("1e6", Precision.Infinite)
val Micro = SciNumber("1e-6", Precision.Infinite)