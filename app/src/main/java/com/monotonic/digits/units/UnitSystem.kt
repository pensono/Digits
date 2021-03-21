package com.monotonic.digits.units

import com.monotonic.digits.R
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
            AtomicHumanUnit("m", R.string.unit_name_meters, null, length),
            AtomicHumanUnit("in", R.string.unit_name_inches, "¹⁄₁₂ft", length, SciNumber.Real(0.0254)),
            AtomicHumanUnit("ft", R.string.unit_name_feet, "12in", length, SciNumber.Real(0.3048)),
            AtomicHumanUnit("mi", R.string.unit_name_miles, "5280ft", length, SciNumber.Real(1609.344)),
            AtomicHumanUnit("au", R.string.unit_name_astronomical_Units, "149,597,870,700m", length, SciNumber.Real("149597870700")),
            AtomicHumanUnit("pc", R.string.unit_name_parsecs, "648,000/π au", length, (SciNumber.Real("3.08567758149137") * SciNumber.Real(10).pow(SciNumber.Real(16))) as SciNumber.Real),
            AtomicHumanUnit("ly", R.string.unit_name_light_years, "9.46ᴇ12km", length, SciNumber.Real("9460730472580800")),

            AtomicHumanUnit("L", R.string.unit_name_liters, "1000cm³", volume, SciNumber.Real(1000).reciprocal()),

            AtomicHumanUnit("rad", R.string.unit_name_radians, "m/m", angle),
            AtomicHumanUnit("°", R.string.unit_name_degrees, "m/m", angle, SciNumber.Real(Math.PI / 180.0)),
            AtomicHumanUnit("sr", R.string.unit_name_steradian, "m²/m²", solid_angle),

            AtomicHumanUnit("ac", R.string.unit_name_acre, "4840yd²", area, SciNumber.Real("4046.86")),
            AtomicHumanUnit("a", R.string.unit_name_are, "100m²", area, SciNumber.Real(100)),
            AtomicHumanUnit("ha", R.string.unit_name_hectare, "10,000m²", area, SciNumber.Real(10000)),

            AtomicHumanUnit("s", R.string.unit_name_seconds, null, time),
            AtomicHumanUnit("min", R.string.unit_name_minutes, "60s", time, SciNumber.Real(60)),
            AtomicHumanUnit("hr", R.string.unit_name_hours, "60min", time, SciNumber.Real(60 * 60)),
            AtomicHumanUnit("day", R.string.unit_name_days, "24hr", time, SciNumber.Real(60 * 60 * 24)),
            AtomicHumanUnit("yr", R.string.unit_name_julian_years, "365.25day", time, SciNumber.Real(60 * 60 * 24 * (365 * 4 + 1) / 4)),

            AtomicHumanUnit("Hz", R.string.unit_name_hertz, "s⁻¹", frequency),
            AtomicHumanUnit("g", R.string.unit_name_grams, null, mass),
            AtomicHumanUnit("N", R.string.unit_name_newtons, "kgm/s²", force),
            AtomicHumanUnit("Pa", R.string.unit_name_pascals, "N/m²", pressure),
            AtomicHumanUnit("mol", R.string.unit_name_moles, null, molarity),

            AtomicHumanUnit("b", R.string.unit_name_bits, null, data),

            AtomicHumanUnit("V", R.string.unit_name_volts, "W/A", emf),
            AtomicHumanUnit("A", R.string.unit_name_amps, null, current),
            AtomicHumanUnit("Ω", R.string.unit_name_ohms, "V/A", impedance),
            AtomicHumanUnit("S", R.string.unit_name_siemens, "Ω⁻¹", conductance),
            AtomicHumanUnit("F", R.string.unit_name_farads, "C/V", capacitance),
            AtomicHumanUnit("W", R.string.unit_name_watts, "J/s", power),
            AtomicHumanUnit("T", R.string.unit_name_teslas, "Wb/m²", magnetic_flux_density),
            AtomicHumanUnit("Wb", R.string.unit_name_webers, "Vs", magnetic_flux),
            AtomicHumanUnit("H", R.string.unit_name_henries, "Wb/A", inductance),
            AtomicHumanUnit("C", R.string.unit_name_coulombs, "sA", charge),

            AtomicHumanUnit("hp", R.string.unit_name_horsepower, "33 000ft lbf/min", power, SciNumber.Real(745.69987158227022)),

            AtomicHumanUnit("atm", R.string.unit_name_atmospheres, "101325 Pa", pressure, SciNumber.Real("101325")),
            AtomicHumanUnit("at", R.string.unit_name_technical_atmospheres, "98.0665 kPa", pressure, SciNumber.Real("98.0665")),
            AtomicHumanUnit("Torr", R.string.unit_name_torres, "¹⁄₇₆₀ atm", pressure, SciNumber.Real("0.007500638")),
            AtomicHumanUnit("bar", R.string.unit_name_bar, "100 kPa", pressure, SciNumber.Real("0.007500638")),

            AtomicHumanUnit("J", R.string.unit_name_joule, "Nm", energy),
            AtomicHumanUnit("eV", R.string.unit_name_electron_volt, "1V×e", energy, SciNumber.Real("1.602176634e-19")),
            AtomicHumanUnit("cal", R.string.unit_name_calorie, "4.184 J", energy, SciNumber.Real("4.184")),
            AtomicHumanUnit("BTU", R.string.unit_name_iso_british_thermal_units, "11055.06 J", energy, SciNumber.Real("1055.06")),

            //AtomicHumanUnit("eV", R.string.unit_name_Electronvolts, null, mass), // The abbreviation should be eV/c2, and we can't parse that night now
            AtomicHumanUnit("u", R.string.unit_name_unified_atomic_mass_units, null, mass, (SciNumber.Real("1.66053906660") * SciNumber.Real(10).pow(SciNumber.Real(-27))) as SciNumber.Real),
            AtomicHumanUnit("Da", R.string.unit_name_dalton, null, mass, (SciNumber.Real("1.66053906660") * SciNumber.Real(10).pow(SciNumber.Real(-27))) as SciNumber.Real),
            AtomicHumanUnit("lb", R.string.unit_name_pound, null, mass, SciNumber.Real("453.5924")),

            AtomicHumanUnit("c", R.string.unit_name_us_customary_cup, "¹⁄₂pt", volume, SciNumber.Real("0.0002365882365")),
            AtomicHumanUnit("cup", R.string.unit_name_us_customary_cup, "¹⁄₂pt", volume, SciNumber.Real("0.0002365882365")),
            AtomicHumanUnit("tsp", R.string.unit_name_us_teaspoon, "¹⁄₂floz", volume, SciNumber.Real("0.00001478676478125")),
            AtomicHumanUnit("tbsp", R.string.unit_name_us_tablespoon, "3tsp", volume, SciNumber.Real("0.00004436029434375")),
            AtomicHumanUnit("floz", R.string.unit_name_us_fluid_ounce, "¹⁄₁₂₈gal", volume, SciNumber.Real("0.0000295735295625")),
            AtomicHumanUnit("pt", R.string.unit_name_us_pint, "16floz", volume, SciNumber.Real("0.000473176473")),
            AtomicHumanUnit("qt", R.string.unit_name_us_quart, "2pt³", volume, SciNumber.Real("0.000946352946")),
            AtomicHumanUnit("gal", R.string.unit_name_us_gallon, "231in³", volume, SciNumber.Real("0.003785411784"))
    )

    val prefixMagStart = -24
    val prefixes = listOf(
            PrefixUnit("Y", R.string.prefix_name_yotta, 24, R.string.prefix_description_yotta),
            PrefixUnit("Z", R.string.prefix_name_zetta, 21, R.string.prefix_description_zetta),
            PrefixUnit("E", R.string.prefix_name_exa, 18, R.string.prefix_description_exa),
            PrefixUnit("P", R.string.prefix_name_peta, 15, R.string.prefix_description_peta),
            PrefixUnit("T", R.string.prefix_name_tera, 12, R.string.prefix_description_tera),
            PrefixUnit("G", R.string.prefix_name_giga, 9, R.string.prefix_description_giga),
            PrefixUnit("M", R.string.prefix_name_mega, 6, R.string.prefix_description_mega),
            PrefixUnit("k", R.string.prefix_name_kilo, 3, R.string.prefix_description_kilo),
            PrefixUnit.One,
            PrefixUnit("c", R.string.prefix_name_centi, -2, R.string.prefix_description_centi),
            PrefixUnit("m", R.string.prefix_name_milli, -3, R.string.prefix_description_milli),
            PrefixUnit("μ", R.string.prefix_name_micro, -6, R.string.prefix_description_micro),
            PrefixUnit("n", R.string.prefix_name_nano, -9, R.string.prefix_description_nano),
            PrefixUnit("p", R.string.prefix_name_pico, -12, R.string.prefix_description_pico),
            PrefixUnit("f", R.string.prefix_name_femto, -15, R.string.prefix_description_femto),
            PrefixUnit("a", R.string.prefix_name_atto, -18, R.string.prefix_description_atto),
            PrefixUnit("z", R.string.prefix_name_zepto, -21, R.string.prefix_description_zepto),
            PrefixUnit("y", R.string.prefix_name_yocto, -24, R.string.prefix_description_yocto)
    )

    val unitAbbreviations = units.associateBy(AtomicHumanUnit::abbreviation)
    val prefixAbbreviations = prefixes.associateBy(AtomicHumanUnit::abbreviation)
}
