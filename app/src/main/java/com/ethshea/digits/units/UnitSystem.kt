package com.ethshea.digits.units

import com.ethshea.digits.SciNumber

object UnitSystem { // Preferred Units?
    val length = mapOf("length" to 1)
    val area = mapOf("length" to 2)
    val time = mapOf("time" to 1)
    val frequency = mapOf("time" to -1)
    val mass = mapOf("mass" to 1)
    val current = mapOf("current" to 1)
    val emf = mapOf("mass" to 1, "length" to 2, "time" to -3, "current" to -1)
    val impedance = mapOf("mass" to 1, "length" to 2, "time" to -3, "current" to -2)
    val tt = mapOf<String, Int>()

    val void = NaturalUnit(mapOf()) // Called this to avoid confusion with "unit", the intended name

    val units = listOf(
            AtomicHumanUnit("m", "meter", length),
            // AtomicHumanUnit("ft", "foot", length, SciNumber("3.28084")),
            // AtomicHumanUnit("mi", "mile", length, SciNumber("0.000621371")),

            AtomicHumanUnit("acre", "acre", area, SciNumber("4046.86")),

            AtomicHumanUnit("s", "second", time),
            AtomicHumanUnit("min", "minute", time, SciNumber(60).reciprocal()),
            AtomicHumanUnit("hr", "hour", time, SciNumber(60 * 60).reciprocal()),

            AtomicHumanUnit("Hz", "hertz", frequency),
            AtomicHumanUnit("g", "gram", mass),

            AtomicHumanUnit("V", "volts", emf),
            AtomicHumanUnit("A", "amps", current),
            AtomicHumanUnit("Ω", "ohms", impedance)
    )

    val prefixes = listOf(
            PrefixUnit("f", "femto", SciNumber("1e-15")),
            PrefixUnit("p", "pico", SciNumber("1e-12")),
            PrefixUnit("n", "nano", SciNumber("1e-9")),
            PrefixUnit("μ", "micro", SciNumber.Micro),
            PrefixUnit("m", "milli", SciNumber.Milli),
            PrefixUnit("k", "kilo", SciNumber.Kilo),
            PrefixUnit("M", "mega", SciNumber.Mega),
            PrefixUnit("G", "giga", SciNumber("1e9")),
            PrefixUnit("T", "tera", SciNumber("1e12")),
            PrefixUnit("P", "peta", SciNumber("1e15")),
            PrefixUnit("E", "exa", SciNumber("1e18"))
    )

    val unitAbbreviations = units.associateBy(AtomicHumanUnit::abbreviation)
    val prefixAbbreviations = prefixes.associateBy(AtomicHumanUnit::abbreviation)
}
