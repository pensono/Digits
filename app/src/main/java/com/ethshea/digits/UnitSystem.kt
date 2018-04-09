package com.ethshea.digits

import java.util.*


/**
 * @author Ethan
 */


open class NaturalUnit(val dimensions: Map<String, Int>, val factor: Double) {
    operator fun plus(other: NaturalUnit) : NaturalUnit {
        return NaturalUnit(combineMapsDefault(dimensions, other.dimensions, Int::plus), factor * other.factor)
    }

    operator fun minus(other: NaturalUnit) : NaturalUnit {
        return NaturalUnit(combineMapsDefault(dimensions, other.dimensions, Int::minus), factor / other.factor)
    }

    operator fun unaryMinus() : NaturalUnit {
        return NaturalUnit(dimensions.mapValues { e -> -e.value }, 1 / factor)
    }

    override fun equals(other: Any?) =
        when (other) {
            is NaturalUnit -> dimensions == other.dimensions && factor == other.factor
            else -> false
        }

    fun dimensionallyEqual(other: NaturalUnit) = dimensions == other.dimensions

    override fun hashCode() = dimensions.hashCode() xor factor.hashCode()
    override fun toString() = dimensions.toString() + " " + factor.toString()

    private fun combineMapsDefault(a: Map<String, Int>, b: Map<String, Int>, operation: (Int, Int) -> Int): Map<String, Int> {
        val newMap = mutableMapOf<String, Int>()
        newMap.putAll(a)
        for (entry in b) {
            val newValue = operation.invoke(newMap.getOrDefault(entry.key, 0), entry.value)
            if (newValue == 0) { // Gross
                newMap.remove(entry.key)
            } else {
                newMap[entry.key] = newValue
            }
        }
        return newMap
    }
}

class HumanUnit(val name: String, dimensions: Map<String, Int>, factor: Double) : NaturalUnit(dimensions, factor)

object UnitSystem { // Preferred Units?
    val length = mapOf("length" to 1)
    val area = mapOf("length" to 2)
    val time = mapOf("time" to 1)
    val frequency = mapOf("time" to -1)
    val mass = mapOf("mass" to 1)
    val tt = mapOf<String, Int>() // Lame name

    val void = NaturalUnit(mapOf(), 1.0) // Called this to avoid confusion with "unit", the intended name

    private val units = mapOf(
            "m" to HumanUnit("meter", length, 1.0),
            "ft" to HumanUnit("foot", length, 3.28084),
            "mi" to HumanUnit("mile", length, 0.000621371),

            "acre" to HumanUnit("acre", area, 4046.86),

            "s" to HumanUnit("second", time, 1.0),
            "min" to HumanUnit("minute", time, 1 / 60.0),
            "hr" to HumanUnit("hour", time, 1 / (60.0 * 60.0)),

            "Hz" to HumanUnit("hertz", frequency, 1.0),
            "g" to HumanUnit("gram", mass, 1.0),

            "k" to HumanUnit("kilo", tt, 1e3),
            "M" to HumanUnit("kilo", tt, 1e6)
    )

    fun byAbbreviation(s: String) = units[s]

    val abbreviations = units.keys
}
