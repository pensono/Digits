package com.ethshea.digits.units

import com.ethshea.digits.SciNumber
import java.math.BigDecimal

object UnitSystem { // Preferred Units?
    val length = mapOf("length" to 1)
    val area = mapOf("length" to 2)
    val time = mapOf("time" to 1)
    val frequency = mapOf("time" to -1)
    val mass = mapOf("mass" to 1)
    val current = mapOf("current" to 1)
    val emf = mapOf("mass" to 1, "length" to 2, "time" to -3, "current" to -1)
    val impedance = mapOf("mass" to 1, "length" to 2, "time" to -3, "current" to -2)
    val capacitance = mapOf("mass" to -1, "length" to -2, "time" to 4, "current" to 2)
    val power = mapOf("mass" to 1, "length" to 2, "time" to -3)
    val magnetic_flux = mapOf("mass" to 1, "length" to 2, "time" to 2, "current" to -3)
    val magnetic_flux_density = mapOf("mass" to 1, "time" to 2, "current" to -3)
    val inductance = mapOf("mass" to 1, "length" to 2, "time" to -3)
    val tt = mapOf<String, Int>()

    val void = NaturalUnit(mapOf()) // Called this to avoid confusion with "unit", the intended name

    val units = listOf(
            AtomicHumanUnit("m", "meters", length),
             AtomicHumanUnit("ft", "feet", length, SciNumber("3.28084")),
             AtomicHumanUnit("mi", "miles", length, SciNumber("0.000621371")),

//             AtomicHumanUnit("acre", "acres", area, SciNumber("4046.86")),

            AtomicHumanUnit("s", "seconds", time),
            AtomicHumanUnit("min", "minutes", time, SciNumber(60).reciprocal()),
            AtomicHumanUnit("hr", "hours", time, SciNumber(60 * 60).reciprocal()),

            AtomicHumanUnit("Hz", "hertz", frequency),
            AtomicHumanUnit("g", "grams", mass),

            AtomicHumanUnit("V", "volts", emf),
            AtomicHumanUnit("A", "amps", current),
            AtomicHumanUnit("Ω", "ohms", impedance),
            AtomicHumanUnit("F", "farads", capacitance),
            AtomicHumanUnit("W", "watts", power),
            AtomicHumanUnit("T", "tesla", magnetic_flux_density),
            AtomicHumanUnit("Wb", "weber", magnetic_flux),
            AtomicHumanUnit("H", "henry", inductance)
    )

    val prefixes = listOf(
            PrefixUnit("f", "femto", "1e-15"),
            PrefixUnit("p", "pico", "1e-12"),
            PrefixUnit("n", "nano", "1e-9"),
            PrefixUnit("μ", "micro", "1e-6"),
            PrefixUnit("m", "milli", "1e-3"),
            PrefixUnit("k", "kilo", "1e3"),
            PrefixUnit("M", "mega", "1e6"),
            PrefixUnit("G", "giga", "1e9"),
            PrefixUnit("T", "tera", "1e12"),
            PrefixUnit("P", "peta", "1e15"),
            PrefixUnit("E", "exa", "1e18")
    )

    val unitAbbreviations = units.associateBy(AtomicHumanUnit::abbreviation)
    val prefixAbbreviations = prefixes.associateBy(AtomicHumanUnit::abbreviation)
}
