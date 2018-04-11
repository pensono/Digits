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

class HumanUnit(val name: String, dimensions: Map<String, Int>, factor: BigDecimal) : NaturalUnit(dimensions, factor)

object UnitSystem { // Preferred Units?
    val length = mapOf("length" to 1)
    val area = mapOf("length" to 2)
    val time = mapOf("time" to 1)
    val frequency = mapOf("time" to -1)
    val mass = mapOf("mass" to 1)
    val tt = mapOf<String, Int>()

    val void = NaturalUnit(mapOf(), BigDecimal.ONE) // Called this to avoid confusion with "unit", the intended name

    private val units = mapOf(
            "m" to HumanUnit("meter", length, BigDecimal.ONE),
            "ft" to HumanUnit("foot", length, BigDecimal("3.28084")),
            "mi" to HumanUnit("mile", length, BigDecimal("0.000621371")),

            "acre" to HumanUnit("acre", area, BigDecimal("4046.86")),

            "s" to HumanUnit("second", time, BigDecimal.ONE),
            "min" to HumanUnit("minute", time, BigDecimal.ONE.divide(BigDecimal(60), MathContext.DECIMAL32)),
            "hr" to HumanUnit("hour", time, BigDecimal.ONE.divide(BigDecimal(60 * 60), MathContext.DECIMAL32)),

            "Hz" to HumanUnit("hertz", frequency, BigDecimal.ONE),
            "g" to HumanUnit("gram", mass, BigDecimal.ONE),

            "k" to HumanUnit("kilo", tt, BigDecimal("1e3")),
            "M" to HumanUnit("mega", tt, BigDecimal("1e6"))
    )

    fun byAbbreviation(s: String) = units[s]

    fun humanAbbreviation(unit: NaturalUnit) : String? {
        for (unitEntry in units) {
            if (unitEntry.value == unit) {
                return unitEntry.key
            }
        }
        return null
    }

    val abbreviations = units.keys
}
