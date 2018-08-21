package com.ethshea.digits

import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.absoluteValue


/**
 * @author Ethan
 */


open class NaturalUnit(open val dimensions: Map<String, Int> = mapOf(), open val factor: BigDecimal = BigDecimal.ONE) {
    operator fun plus(other: NaturalUnit) =
            NaturalUnit(combineMapsDefault(dimensions, other.dimensions, Int::plus), factor * other.factor)

    operator fun minus(other: NaturalUnit) =
        NaturalUnit(combineMapsDefault(dimensions, other.dimensions, Int::minus), factor.divide(other.factor))

    operator fun times(n: Int) : NaturalUnit =
        NaturalUnit(dimensions.mapValues { entry -> entry.value * n }, factor.pow(n))

    operator fun unaryMinus() =
        NaturalUnit(dimensions.mapValues { e -> -e.value }, BigDecimal.ONE.divide(factor))

    override fun equals(other: Any?) =
        other is NaturalUnit && representationEqual(other)

    fun representationEqual(other: NaturalUnit) = dimensionallyEqual(other) && factor == other.factor
    fun dimensionallyEqual(other: NaturalUnit) = dimensions == other.dimensions

    /**
     * Returns true if all dimensions of this unit are greater than or equal to the other
     */
    fun fitsWithin(other: NaturalUnit) : Boolean {
        if (!other.dimensions.keys.containsAll(dimensions.keys)
            || !fitsWithin(factor, other.factor)) {
            return false
        }

        return dimensions.all { dimension ->
            other.dimensions.containsKey(dimension.key)
            && fitsWithin(dimension.value, other.dimensions[dimension.key]!!)
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

    /**
     * Returns true iff a has a smaller or equal magnitude and the same sign as b
     */
    private fun fitsWithin(a: Int, b: Int) = (Integer.signum(a) == Integer.signum(b)) && (Math.abs(a) <= Math.abs(b))


    /**
     * Returns true iff fitsWithin(log10(factorA), log10(factorB))
     */
    private fun fitsWithin(factorA: BigDecimal, factorB: BigDecimal) =
            factorA == BigDecimal.ONE ||
                    if (factorA > BigDecimal.ONE) {
                        factorA <= factorB
                    } else {
                        factorA >= factorB
                    }
}

class AtomicHumanUnit(val abbreviation: String, val name: String, dimensions: Map<String, Int>, factor: BigDecimal = BigDecimal.ONE) : NaturalUnit(dimensions, factor) {
    override fun toString() = abbreviation + " " + super.toString()
}

class HumanUnit(val components: Map<AtomicHumanUnit, Int>) : NaturalUnit() {
    val underlyingUnit =
            components.map { entry -> entry.key * entry.value }
            .fold(UnitSystem.void, NaturalUnit::plus)

    override val dimensions = underlyingUnit.dimensions
    override val factor = underlyingUnit.factor

    val abbreviation = components.map { entry -> entry.key.abbreviation + prettyExponent(entry.value) }.joinToString("")

    override fun equals(other: Any?) =
        other is HumanUnit && other.components == components

    override fun toString(): String = "HumanUnit($components, $abbreviation)"
}

val superscriptMap = listOf("⁰", "¹", "²", "³", "⁴", "⁵", "⁶", "⁷", "⁸", "⁹")
val superscriptMinus = "⁻"
fun prettyExponent(number: Int) : String {
    if (number == 1) {
        return ""
    }

    val stringRepresentation = number.absoluteValue.toString()
    val superscript = stringRepresentation.map { char -> superscriptMap[char - '0'] }
    val sign = if (number < 0) superscriptMinus else ""

    return sign + superscript
}

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
            AtomicHumanUnit("m", "meter", length),
            AtomicHumanUnit("ft", "foot", length, BigDecimal("3.28084")),
            AtomicHumanUnit("mi", "mile", length, BigDecimal("0.000621371")),

            AtomicHumanUnit("acre", "acre", area, BigDecimal("4046.86")),

            AtomicHumanUnit("s", "second", time),
            AtomicHumanUnit("min", "minute", time, BigDecimal.ONE.divide(BigDecimal(60), MathContext.DECIMAL32)),
            AtomicHumanUnit("hr", "hour", time, BigDecimal.ONE.divide(BigDecimal(60 * 60), MathContext.DECIMAL32)),

            AtomicHumanUnit("Hz", "hertz", frequency),
            AtomicHumanUnit("g", "gram", mass),

            AtomicHumanUnit("V", "volts", emf),
            AtomicHumanUnit("A", "amps", current),
            AtomicHumanUnit("Ω", "ohms", impedance)
    )

    val prefixUnits = listOf(
            AtomicHumanUnit("μ", "micro", tt, BigDecimal("1e-3", MathContext.DECIMAL128)),
            AtomicHumanUnit("M", "mega", tt, BigDecimal("1e6", MathContext.DECIMAL128)),
            AtomicHumanUnit("k", "kilo", tt, BigDecimal("1e3", MathContext.DECIMAL128))
    )

    private val unitAbbreviations = (prefixUnits + units).associateBy(AtomicHumanUnit::abbreviation)

    fun byAbbreviation(s: String) = unitAbbreviations[s]

    val abbreviations = unitAbbreviations.keys
}
