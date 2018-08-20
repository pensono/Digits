package com.ethshea.digits

import java.math.BigDecimal
import java.math.MathContext


/**
 * @author Ethan
 */


open class NaturalUnit(val dimensions: Map<String, Int> = mapOf(), val factor: BigDecimal = BigDecimal.ONE) {
    operator fun plus(other: NaturalUnit) : NaturalUnit {
        return NaturalUnit(combineMapsDefault(dimensions, other.dimensions, Int::plus), factor * other.factor)
    }

    operator fun minus(other: NaturalUnit) : NaturalUnit {
        return NaturalUnit(combineMapsDefault(dimensions, other.dimensions, Int::minus), factor / other.factor)
    }

    operator fun unaryMinus() : NaturalUnit {
        return NaturalUnit(dimensions.mapValues { e -> -e.value }, BigDecimal.ONE.divide(factor))
    }

    override fun equals(other: Any?) =
        when (other) {
            is NaturalUnit -> dimensions == other.dimensions && factor == other.factor
            else -> false
        }

    fun dimensionallyEqual(other: NaturalUnit) = dimensions == other.dimensions

    /**
     * Returns true if all dimensions of this unit are greater than or equal to the other
     */
    fun fits(other: NaturalUnit) : Boolean {
        if (!dimensions.keys.containsAll(other.dimensions.keys)) {
            return false
        }

        return dimensions.all { dimension ->
            other.dimensions.containsKey(dimension.key)
            && (Integer.signum(dimension.value) == Integer.signum(other.dimensions[dimension.key]!!))
            && (Math.abs(dimension.value) >= Math.abs(other.dimensions[dimension.key]!!))
        }
    }

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

class HumanUnit(val abbreviation: String, val name: String, dimensions: Map<String, Int>, factor: BigDecimal = BigDecimal.ONE) : NaturalUnit(dimensions, factor)

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

    val void = NaturalUnit(mapOf(), BigDecimal.ONE) // Called this to avoid confusion with "unit", the intended name

    val units = listOf(
            HumanUnit("m", "meter", length),
            HumanUnit("ft", "foot", length, BigDecimal("3.28084")),
            HumanUnit("mi", "mile", length, BigDecimal("0.000621371")),

            HumanUnit("acre", "acre", area, BigDecimal("4046.86")),

            HumanUnit("s", "second", time),
            HumanUnit("min", "minute", time, BigDecimal.ONE.divide(BigDecimal(60), MathContext.DECIMAL32)),
            HumanUnit("hr", "hour", time, BigDecimal.ONE.divide(BigDecimal(60 * 60), MathContext.DECIMAL32)),

            HumanUnit("Hz", "hertz", frequency),
            HumanUnit("g", "gram", mass),

            HumanUnit("V", "volts", emf),
            HumanUnit("A", "amps", current),
            HumanUnit("Ω", "ohms", impedance),

            HumanUnit("k", "kilo", tt, BigDecimal("1e3")),
            HumanUnit("M", "mega", tt, BigDecimal("1e6"))
    )
    
    private val unitAbbreviations = units.associateBy(HumanUnit::abbreviation)

    fun byAbbreviation(s: String) = unitAbbreviations[s]

    val abbreviations = unitAbbreviations.keys
}
