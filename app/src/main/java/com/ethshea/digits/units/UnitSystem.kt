package com.ethshea.digits.units

import com.ethshea.digits.evaluator.SciNumber
import com.ethshea.digits.human.AtomicHumanUnit

object UnitSystem { // Preferred Units?
    val length = mapOf("length" to 1)
    val area = mapOf("length" to 2)
    val volume = mapOf("length" to 3)
    val time = mapOf("time" to 1)
    val frequency = mapOf("time" to -1)
    val angle = mapOf("length" to 1, "circular" to 1) // Circular is orthogonal to length
    val solid_angle = mapOf("length" to 1, "circular" to 1) // Circular is orthogonal to length
    val mass = mapOf("mass" to 1)
    val current = mapOf("current" to 1)
    val emf = mapOf("mass" to 1, "length" to 2, "time" to -3, "current" to -1)
    val impedance = mapOf("mass" to 1, "length" to 2, "time" to -3, "current" to -2)
    val conductance = mapOf("mass" to -1, "length" to -2, "time" to 3, "current" to 2)
    val capacitance = mapOf("mass" to -1, "length" to -2, "time" to 4, "current" to 2)
    val power = mapOf("mass" to 1, "length" to 2, "time" to -3)
    val magnetic_flux = mapOf("mass" to 1, "length" to 2, "time" to 2, "current" to -3)
    val magnetic_flux_density = mapOf("mass" to 1, "time" to 2, "current" to -3)
    val inductance = mapOf("mass" to 1, "length" to 2, "time" to -3)
    val force = mapOf("mass" to 1, "length" to 1, "time" to -2)
    val charge = mapOf("current" to 1, "time" to 1)
    val pressure = mapOf("mass" to 1, "time" to -2, "length" to 1)
    val data = mapOf("data" to 1)
    val morarity = mapOf("molarity" to 1)

    val tt = mapOf<String, Int>()

    val void = NaturalUnit(mapOf()) // Called this to avoid confusion with "unit", the intended name

    val units = listOf(
            AtomicHumanUnit("m", "Meters", null, length),
//            AtomicHumanUnit("ft", "feet", length, Real("3.28084")),
//            AtomicHumanUnit("mi", "miles", length, Real("0.000621371")),
//            AtomicHumanUnit("L", "liters", length, Real(1000).reciprocal()),

            AtomicHumanUnit("rad", "Radians", "m/m", angle),
            AtomicHumanUnit("°", "Degrees", "m/m", angle, SciNumber.Real(Math.PI / 180.0)),
            AtomicHumanUnit("sr", "Steradian", "m²/m²", solid_angle),

//             AtomicHumanUnit("acre", "acres", area, Real("4046.86")),

            AtomicHumanUnit("s", "Seconds", null, time),
//            AtomicHumanUnit("min", "minutes", time, Real(60).reciprocal()),
//            AtomicHumanUnit("hr", "hours", time, Real(60 * 60).reciprocal()),

            AtomicHumanUnit("Hz", "Hertz", "s⁻¹", frequency),
            AtomicHumanUnit("g", "Grams", null, mass),
            AtomicHumanUnit("N", "Newtons", "kgm/s²", force),
            AtomicHumanUnit("Pa", "Pascals", "N/m²", pressure),
            AtomicHumanUnit("mol", "Moles", null, pressure),

            AtomicHumanUnit("b", "Bits", null, data),

            AtomicHumanUnit("V", "Volts", "W/A", emf),
            AtomicHumanUnit("A", "Amps", null, current),
            AtomicHumanUnit("Ω", "Ohms", "V/A", impedance),
            AtomicHumanUnit("S", "Siemens", "Ω⁻¹", conductance),
            AtomicHumanUnit("F", "Farads", "C/V", capacitance),
            AtomicHumanUnit("W", "Watts", "J/s", power),
            AtomicHumanUnit("T", "Tesla", "Wb/m²", magnetic_flux_density),
            AtomicHumanUnit("Wb", "Weber", "Vs", magnetic_flux),
            AtomicHumanUnit("H", "Henry", "Wb/A", inductance),
            AtomicHumanUnit("C", "Coulomb", "sA", charge)
    )

    val prefixMagStart = -15
    val prefixes = listOf(
            PrefixUnit("f", "Femto", "1e-15"),
            PrefixUnit("p", "Pico", "1e-12"),
            PrefixUnit("n", "Nano", "1e-9"),
            PrefixUnit("μ", "Micro", "1e-6"),
            PrefixUnit("m", "Milli", "1e-3"),
            PrefixUnit("", "One", "1e0"),
            PrefixUnit("k", "Kilo", "1e3"),
            PrefixUnit("M", "Mega", "1e6"),
            PrefixUnit("G", "Giga", "1e9"),
            PrefixUnit("T", "Tera", "1e12"),
            PrefixUnit("P", "Peta", "1e15"),
            PrefixUnit("E", "Exa", "1e18")
    )

    val unitAbbreviations = units.associateBy(AtomicHumanUnit::abbreviation)
    val prefixAbbreviations = prefixes.associateBy(AtomicHumanUnit::abbreviation)
}
