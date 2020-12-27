package com.monotonic.digits.units

import com.monotonic.digits.evaluator.SciNumber
import com.monotonic.digits.human.AtomicHumanUnit

object UnitSystem { // Preferred Units?
    val length = DimensionBag("length" to 1)
    val area = DimensionBag("length" to 2)
    val volume = DimensionBag("length" to 3)
    val time = DimensionBag("time" to 1)
    val frequency = DimensionBag("time" to -1)
    val angle = DimensionBag("angle" to 1)
    val solid_angle = DimensionBag("angle" to 2)
    val mass = DimensionBag("mass" to 1)
    val current = DimensionBag("current" to 1)
    val emf = DimensionBag("mass" to 1, "length" to 2, "time" to -3, "current" to -1)
    val impedance = DimensionBag("mass" to 1, "length" to 2, "time" to -3, "current" to -2)
    val conductance = DimensionBag("mass" to -1, "length" to -2, "time" to 3, "current" to 2)
    val capacitance = DimensionBag("mass" to -1, "length" to -2, "time" to 4, "current" to 2)
    val power = DimensionBag("mass" to 1, "length" to 2, "time" to -3)
    val energy = DimensionBag("mass" to 1, "length" to 2, "time" to -2)
    val magnetic_flux = DimensionBag("mass" to 1, "length" to 2, "time" to 2, "current" to -3)
    val magnetic_flux_density = DimensionBag("mass" to 1, "time" to 2, "current" to -3)
    val inductance = DimensionBag("mass" to 1, "length" to 2, "time" to -2, "current" to -2)
    val force = DimensionBag("mass" to 1, "length" to 1, "time" to -2)
    val charge = DimensionBag("current" to 1, "time" to 1)
    val pressure = DimensionBag("mass" to 1, "time" to -2, "length" to 1)
    val data = DimensionBag("data" to 1)
    val molarity = DimensionBag("molarity" to 1)

    val tt = mapOf<String, Int>()

    // Not 100% sure about the fact that this lives here. Should it be NaturalUnit.Void?
    val void = NaturalUnit(mapOf()) // Called this to avoid confusion with "unit", the intended name

    val units = listOf(
            AtomicHumanUnit("m", "Meters", null, length),
            AtomicHumanUnit("in", "Inches", "¹⁄₁₂ft", length, SciNumber.Real(0.0254)),
            AtomicHumanUnit("ft", "Feet", "12in", length, SciNumber.Real(0.3048)),
            AtomicHumanUnit("mi", "Miles", "5280ft", length, SciNumber.Real(1609.344)),
            AtomicHumanUnit("au", "Astronomical Units", "149,597,870,700m", length, SciNumber.Real("149597870700")),
            AtomicHumanUnit("pc", "Parsecs", "648,000/π au", length, (SciNumber.Real("3.08567758149137") * SciNumber.Real(10).pow(SciNumber.Real(16))) as SciNumber.Real),
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
            AtomicHumanUnit("u", "Unified Atomic Mass Units", null, mass, (SciNumber.Real("1.66053906660") * SciNumber.Real(10).pow(SciNumber.Real(-27))) as SciNumber.Real),
            AtomicHumanUnit("Da", "Dalton", null, mass, (SciNumber.Real("1.66053906660") * SciNumber.Real(10).pow(SciNumber.Real(-27))) as SciNumber.Real),
            AtomicHumanUnit("lb", "Pound", null, mass, SciNumber.Real("453.5924")),

            AtomicHumanUnit("c", "US Customary Cup", "¹⁄₂pt", volume, SciNumber.Real("0.0002365882365")),
            AtomicHumanUnit("cup", "US Customary Cup", "¹⁄₂pt", volume, SciNumber.Real("0.0002365882365")),
            AtomicHumanUnit("tsp", "US Teaspoon", "¹⁄₂floz", volume, SciNumber.Real("0.00001478676478125")),
            AtomicHumanUnit("tbsp", "US Tablespoon", "3tsp", volume, SciNumber.Real("0.00004436029434375")),
            AtomicHumanUnit("floz", "US Fluid Ounce", "¹⁄₁₂₈gal", volume, SciNumber.Real("0.0000295735295625")),
            AtomicHumanUnit("pt", "US Pint", "16floz", volume, SciNumber.Real("0.000473176473")),
            AtomicHumanUnit("qt", "US Quart", "2pt³", volume, SciNumber.Real("0.000946352946")),
            AtomicHumanUnit("gal", "US Gallon", "231in³", volume, SciNumber.Real("0.003785411784"))
    )

    val prefixMagStart = -24
    val prefixes = listOf(
            PrefixUnit("Y", "Yotta", 24, "Septillion"),
            PrefixUnit("Z", "Zetta", 21, "Sextillion"),
            PrefixUnit("E", "Exa", 18, "Quintillion"),
            PrefixUnit("P", "Peta", 15, "Quadrillion"),
            PrefixUnit("T", "Tera", 12, "Trillion"),
            PrefixUnit("G", "Giga", 9, "Billion"),
            PrefixUnit("M", "Mega", 6, "Million"),
            PrefixUnit("k", "Kilo", 3, "Thousand"),
            PrefixUnit.One,
            PrefixUnit("c", "Centi", -2, "Hundredth"),
            PrefixUnit("m", "Milli", -3, "Thousandth"),
            PrefixUnit("μ", "Micro", -6, "Millionth"),
            PrefixUnit("n", "Nano", -9, "Trillionth"),
            PrefixUnit("p", "Pico", -12, "Billionth"),
            PrefixUnit("f", "Femto", -15, "Quadrillionth"),
            PrefixUnit("a", "Atto", -18, "Quintillionth"),
            PrefixUnit("z", "Zepto", -21, "Sextillionth"),
            PrefixUnit("y", "Yocto", -24, "Septillionth")
    )

    val unitAbbreviations = units.associateBy(AtomicHumanUnit::abbreviation)
    val prefixAbbreviations = prefixes.associateBy(AtomicHumanUnit::abbreviation)
}
