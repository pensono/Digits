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
            AtomicHumanUnit("T", "Teslas", "Wb/m²", magnetic_flux_density),
            AtomicHumanUnit("Wb", "Webers", "Vs", magnetic_flux),
            AtomicHumanUnit("H", "Henries", "Wb/A", inductance),
            AtomicHumanUnit("C", "Coulombs", "sA", charge)
    )

    val prefixMagStart = -24
    val prefixes = listOf(
            PrefixUnit("y", "Yocto", "1e-24", "Septillionth"),
            PrefixUnit("z", "Zepto", "1e-21", "Sextillionth"),
            PrefixUnit("a", "Atto", "1e-18", "Quintillionth"),
            PrefixUnit("f", "Femto", "1e-15", "Quadrillionth"),
            PrefixUnit("p", "Pico", "1e-12", "Billionth"),
            PrefixUnit("n", "Nano", "1e-9", "Trillionth"),
            PrefixUnit("μ", "Micro", "1e-6", "Millionth"),
            PrefixUnit("m", "Milli", "1e-3", "Thousandth"),
            PrefixUnit.One,
            PrefixUnit("k", "Kilo", "1e3", "Thousand"),
            PrefixUnit("M", "Mega", "1e6", "Million"),
            PrefixUnit("G", "Giga", "1e9", "Billion"),
            PrefixUnit("T", "Tera", "1e12", "Trillion"),
            PrefixUnit("P", "Peta", "1e15", "Quadrillion"),
            PrefixUnit("E", "Exa", "1e18", "Quintillion"),
            PrefixUnit("Z", "Zetta", "1e21", "Sextillion"),
            PrefixUnit("Y", "Yotta", "1e24", "Septillion")
    )

    val unitAbbreviations = units.associateBy(AtomicHumanUnit::abbreviation)
    val prefixAbbreviations = prefixes.associateBy(AtomicHumanUnit::abbreviation)
}
