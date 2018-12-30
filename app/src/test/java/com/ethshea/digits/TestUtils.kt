package com.ethshea.digits

import com.ethshea.digits.evaluator.Precision
import com.ethshea.digits.evaluator.Quantity
import com.ethshea.digits.evaluator.SciNumber
import com.ethshea.digits.units.NaturalUnit
import com.ethshea.digits.units.UnitSystem


fun q(value: String, unit: NaturalUnit) = Quantity(SciNumber.Real(value), unit)
fun u(abbr: String) = UnitSystem.unitAbbreviations[abbr]!!
fun p(abbr: String) = UnitSystem.prefixAbbreviations[abbr]!!
fun sf(i: Int) = Precision.SigFigs(i)

val Kilo = SciNumber.Real("1e3", Precision.Infinite)
val Milli = SciNumber.Real("1e-3", Precision.Infinite)
val Mega = SciNumber.Real("1e6", Precision.Infinite)
val Micro = SciNumber.Real("1e-6", Precision.Infinite)