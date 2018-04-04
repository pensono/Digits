package com.ethshea.unitcalculator.dimensional

/**
 * @author Ethan
 */


open class NaturalUnit(val dimensions: Map<String, Int>, val factor: Double) {

    // TODO: refactor plus and minus with a map-merge operation
    operator fun plus(other: NaturalUnit) : NaturalUnit {
        val newMap = mutableMapOf<String, Int>()
        newMap.putAll(dimensions)
        for (entry in other.dimensions) {
            newMap[entry.key] = newMap.getOrDefault(entry.key, 0) + entry.value
        }

        return NaturalUnit(newMap, factor * other.factor)
    }

    operator fun minus(other: NaturalUnit) : NaturalUnit {
        val newMap = mutableMapOf<String, Int>()
        newMap.putAll(dimensions)
        for (entry in other.dimensions) {
            newMap[entry.key] = newMap.getOrDefault(entry.key, 0) - entry.value
        }

        return NaturalUnit(newMap, factor / other.factor)
    }

    operator fun unaryMinus() : NaturalUnit {
        return NaturalUnit(dimensions.mapValues { e ->  -e.value }, 1/factor)
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is NaturalUnit -> dimensions == other.dimensions && factor == other.factor
            else -> false
        }
    }

    override fun hashCode(): Int {
        return dimensions.hashCode() xor factor.hashCode()
    }

    override fun toString(): String {
        return dimensions.toString() + " " + factor.toString()
    }
}

class HumanUnit(val name: String, dimensions: Map<String, Int>, factor: Double) : NaturalUnit(dimensions, factor)

object UnitSystem { // Preferred Units?
    private val length = mapOf("length" to 1)
    private val area = mapOf("length" to 2)
    private val time = mapOf("time" to 1)
    private val frequency = mapOf("time" to -1)
    private val mass = mapOf("mass" to 1)
    private val tt = mapOf<String, Int>() // Lame name

    private val void = NaturalUnit(mapOf(), 1.0) // Called this to avoid confusion with "unit", the intended name

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

    fun fromString(input: String) : NaturalUnit? {
        var remaining = input
        var invert = false
        var unit = void

        outer@ while (remaining.isNotEmpty()) {
            for (entry in units.entries.sortedBy { -it.key.length }) {
                if (remaining.startsWith(entry.key)) {
                    unit += if (invert) -entry.value else entry.value
                    remaining = remaining.substring(entry.key.length)
                    continue@outer
                }
            }

            if (remaining.startsWith("/")) {
                invert = true
                remaining = remaining.substring(1)
            } else {
                return null
            }
        }

        return unit
    }
}
