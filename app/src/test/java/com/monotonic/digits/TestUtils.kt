package com.monotonic.digits

import com.monotonic.digits.evaluator.Precision
import com.monotonic.digits.evaluator.Quantity
import com.monotonic.digits.evaluator.SciNumber
import com.monotonic.digits.units.NaturalUnit
import com.monotonic.digits.units.UnitSystem


fun q(value: String, unit: NaturalUnit) = Quantity(SciNumber.Real(value), unit)
fun u(abbr: String) = UnitSystem.unitAbbreviations[abbr]!!
fun p(abbr: String) = UnitSystem.prefixAbbreviations[abbr]!!
fun sf(i: Int) = Precision.SigFigs(i)

val Kilo = SciNumber.Real("1e3", Precision.Infinite)
val Milli = SciNumber.Real("1e-3", Precision.Infinite)
val Mega = SciNumber.Real("1e6", Precision.Infinite)
val Micro = SciNumber.Real("1e-6", Precision.Infinite)