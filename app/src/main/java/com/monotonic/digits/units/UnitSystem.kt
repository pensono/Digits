package com.monotonic.digits.units

import com.monotonic.digits.evaluator.SciNumber
import com.monotonic.digits.human.AtomicHumanUnit

object UnitSystem { // Preferred Units?
    val length = mapOf("length" to 1)
    val area = mapOf("length" to 2)
    val volume = mapOf("length" to 3)
    val time = mapOf("time" to 1)
    val frequency = mapOf("time" to -1)
    val angle = mapOf("angle" to 1)
    val solid_angle = mapOf("angle" to 2)
    val mass = mapOf("mass" to 1)
    val current = mapOf("current" to 1)
    val emf = mapOf("mass" to 1, "length" to 2, "time" to -3, "current" to -1)
    val impedance = mapOf("mass" to 1, "length" to 2, "time" to -3, "current" to -2)
    val conductance = mapOf("mass" to -1, "length" to -2, "time" to 3, "current" to 2)
    val capacitance = mapOf("mass" to -1, "length" to -2, "time" to 4, "current" to 2)
    val power = mapOf("mass" to 1, "length" to 2, "time" to -3)
    val energy = mapOf("mass" to 1, "length" to 2, "time" to -2)
    val magnetic_flux = mapOf("mass" to 1, "length" to 2, "time" to 2, "current" to -3)
    val magnetic_flux_density = mapOf("mass" to 1, "time" to 2, "current" to -3)
    val inductance = mapOf("mass" to 1, "length" to 2, "time" to -2, "current" to -2)
    val force = mapOf("mass" to 1, "length" to 1, "time" to -2)
    val charge = mapOf("current" to 1, "time" to 1)
    val pressure = mapOf("mass" to 1, "time" to -2, "length" to 1)
    val data = mapOf("data" to 1)
    val molarity = mapOf("molarity" to 1)

    val tt = mapOf<String, Int>()

    val void = NaturalUnit(mapOf()) // Called this to avoid confusion with "unit", the intended name

    val units = listOf(
            AtomicHumanUnit("m", "Meters", null, length),
            AtomicHumanUnit("in", "Inches", "¹⁄₁₂ft", length, SciNumber.Real(0.0254)),
            AtomicHumanUnit("ft", "Feet", "12in", length, SciNumber.Real(0.3048)),
            AtomicHumanUnit("mi", "Miles", "5280ft", length, SciNumber.Real(1609.344)),
            AtomicHumanUnit("au", "Astronomical Units", "149,597,870,700m", length, SciNumber.Real("149597870700")),
            AtomicHumanUnit("pc", "Parsecs", "648,000/π au", length, (SciNumber.Real("3.08567758149137") * SciNumber.Real(10).pow(16)) as SciNumber.Real),
            AtomicHumanUnit("ly", "Light Years", "9.46ᴇ12km", length, SciNumber.Real("9460730472580800")),

            AtomicHumanUnit("L", "Liters", "1000cm³", volume, SciNumber.Real(1000).reciprocal()),

            AtomicHumanUnit("rad", "Radians", "m/m", angle),
            AtomicHumanUnit("°", "Degrees", "m/m", angle, SciNumber.Real(Math.PI / 180.0)),
            AtomicHumanUnit("sr", "Steradian", "m²/m²", solid_angle),

            AtomicHumanUnit("ac", "Acre", "4840yd²", area, SciNumber.Real("4046.86")),
            AtomicHumanUnit("a", "Are", "100m²", area, SciNumber.Real(100)),
            AtomicHumanUnit("ha", "Hectare", "10,000m²", area, SciNumber.Real(10000)),

            AtomicHumanUnit("s", "Seconds", null, time),
            AtomicHumanUnit("min", "Minutes", "60s", time, SciNumber.Real(60)),
            AtomicHumanUnit("hr", "Hours", "60min", time, SciNumber.Real(60 * 60)),
            AtomicHumanUnit("day", "Days", "24hr", time, SciNumber.Real(60 * 60 * 24)),
            AtomicHumanUnit("yr", "Julian Years", "365.25day", time, SciNumber.Real(60 * 60 * 24 * (365 * 4 + 1) / 4)),

            AtomicHumanUnit("Hz", "Hertz", "s⁻¹", frequency),
            AtomicHumanUnit("g", "Grams", null, mass),
            AtomicHumanUnit("N", "Newtons", "kgm/s²", force),
            AtomicHumanUnit("Pa", "Pascals", "N/m²", pressure),
            AtomicHumanUnit("mol", "Moles", null, molarity),

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
            AtomicHumanUnit("C", "Coulombs", "sA", charge),

            AtomicHumanUnit("hp", "Horsepower", "33 000ft lbf/min", power, SciNumber.Real(745.69987158227022)),

            AtomicHumanUnit("atm", "Atmospheres", "101325 Pa", pressure, SciNumber.Real("101325")),
            AtomicHumanUnit("at", "Technical Atmospheres", "98.0665 kPa", pressure, SciNumber.Real("98.0665")),
            AtomicHumanUnit("Torr", "Torres (mmHg)", "¹⁄₇₆₀ atm", pressure, SciNumber.Real("0.007500638")),
            AtomicHumanUnit("bar", "Bar", "100 kPa", pressure, SciNumber.Real("0.007500638")),

            AtomicHumanUnit("J", "Joule", "Nm", energy),
            AtomicHumanUnit("eV", "Electron Volt", "1V×e", energy, SciNumber.Real("1.602176634e-19")),
            AtomicHumanUnit("cal", "Calorie", "4.184 J", energy, SciNumber.Real("4.184")),
            AtomicHumanUnit("BTU", "ISO British Thermal Units", "11055.06 J", energy, SciNumber.Real("1055.06")),

            //AtomicHumanUnit("eV", "Electronvolts", null, mass), // The abbreviation should be eV/c2, and we can't parse that night now
            AtomicHumanUnit("u", "Unified Atomic Mass Units", null, mass, (SciNumber.Real("1.66053906660") * SciNumber.Real(10).pow(-27)) as SciNumber.Real),
            AtomicHumanUnit("Da", "Dalton", null, mass, (SciNumber.Real("1.66053906660") * SciNumber.Real(10).pow(-27)) as SciNumber.Real),
            AtomicHumanUnit("lb", "Pound", null, mass, SciNumber.Real("0.4535924")),

            AtomicHumanUnit("c", "US Customary Cup", "¹⁄₂pt", volume,    SciNumber.Real("0.0002365882365")),
            AtomicHumanUnit("cup", "US Customary Cup", "¹⁄₂pt", volume,  SciNumber.Real("0.0002365882365")),
            AtomicHumanUnit("tsp", "US Teaspoon", "¹⁄₂floz", volume,     SciNumber.Real("0.00001478676478125")),
            AtomicHumanUnit("tbsp", "US Tablespoon", "3tsp", volume,    SciNumber.Real("0.00004436029434375")),
            AtomicHumanUnit("floz", "US Fluid Ounce", "¹⁄₁₂₈gal", volume, SciNumber.Real("0.0000295735295625")),
            AtomicHumanUnit("pt", "US Pint", "16floz", volume,          SciNumber.Real("0.000473176473")),
            AtomicHumanUnit("qt", "US Quart", "2pt³", volume,           SciNumber.Real("0.000946352946")),
            AtomicHumanUnit("gal", "US Gallon", "231in³", volume,       SciNumber.Real("0.003785411784"))
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
            PrefixUnit("c", "Centi", "1e-2", "Hundredth"),
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
