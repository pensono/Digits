package com.ethshea.digits

import kotlin.math.absoluteValue


/**
 * @author Ethan
 */
open class NaturalUnit(open val dimensions: Map<String, Int> = mapOf(), open val factor: SciNumber = SciNumber.One) {
    operator fun plus(other: NaturalUnit) =
            NaturalUnit(combineMapsDefault(dimensions, other.dimensions, Int::plus), factor * other.factor)

    operator fun minus(other: NaturalUnit) =
        NaturalUnit(combineMapsDefault(dimensions, other.dimensions, Int::minus), factor / other.factor)

    operator fun times(n: Int) : NaturalUnit =
        NaturalUnit(dimensions.mapValues { entry -> entry.value * n }, factor.pow(n))

    operator fun unaryMinus() =
        NaturalUnit(dimensions.mapValues { e -> -e.value }, factor.reciprocal())

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
    private fun fitsWithin(factorA: SciNumber, factorB: SciNumber) =
            factorA == SciNumber.One ||
                    if (factorA > SciNumber.One) {
                        factorA <= factorB
                    } else {
                        factorA >= factorB
                    }
}

class AtomicHumanUnit(val abbreviation: String, val name: String, dimensions: Map<String, Int>, factor: SciNumber = SciNumber.One) : NaturalUnit(dimensions, factor) {
    override fun toString() = abbreviation + " " + super.toString()
}

class HumanUnit(val components: Map<AtomicHumanUnit, Int>) : NaturalUnit() {
    val underlyingUnit =
            components.map { entry -> entry.key * entry.value }
            .fold(UnitSystem.void, NaturalUnit::plus)

    override val dimensions = underlyingUnit.dimensions
    override val factor = underlyingUnit.factor

    val exponentMagnitude = components.values.map{ it.absoluteValue }.sum()

    val abbreviation = components.map { entry -> entry.key.abbreviation + prettyExponent(entry.value) }.joinToString("")

    operator fun plus(other: AtomicHumanUnit) = incorperateUnit(other, 1)
    operator fun minus(other: AtomicHumanUnit) = incorperateUnit(other, -1)

    private fun incorperateUnit(other: AtomicHumanUnit, exponent: Int): HumanUnit {
        val newMap = HashMap(components)
        newMap[other] = newMap.getOrDefault(other, 0) + exponent
        return HumanUnit(newMap)
    }

    override fun equals(other: Any?) =
        other is HumanUnit && other.components == components

    override fun hashCode(): Int = components.hashCode()

    override fun toString(): String = "HumanUnit($components, $abbreviation)"
}

val superscriptMap = listOf("⁰", "¹", "²", "³", "⁴", "⁵", "⁶", "⁷", "⁸", "⁹")
val superscriptMinus = "⁻"
fun prettyExponent(number: Int) : String {
    if (number == 1) {
        return ""
    }

    val stringRepresentation = number.absoluteValue.toString()
    val superscript = stringRepresentation.map { char -> superscriptMap[char - '0'] }.joinToString("")
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

    val void = NaturalUnit(mapOf()) // Called this to avoid confusion with "unit", the intended name

    val units = listOf(
            AtomicHumanUnit("m", "meter", length),
            AtomicHumanUnit("ft", "foot", length, SciNumber("3.28084")),
            AtomicHumanUnit("mi", "mile", length, SciNumber("0.000621371")),

            AtomicHumanUnit("acre", "acre", area, SciNumber("4046.86")),

            AtomicHumanUnit("s", "second", time),
            AtomicHumanUnit("min", "minute", time, SciNumber(60).reciprocal()),
            AtomicHumanUnit("hr", "hour", time, SciNumber(60 * 60).reciprocal()),

            AtomicHumanUnit("Hz", "hertz", frequency),
            AtomicHumanUnit("g", "gram", mass),

            AtomicHumanUnit("V", "volts", emf),
            AtomicHumanUnit("A", "amps", current),
            AtomicHumanUnit("Ω", "ohms", impedance)
    )

    val prefixes = listOf(
            AtomicHumanUnit("f", "femto", tt, SciNumber("1e-15")),
            AtomicHumanUnit("p", "pico", tt, SciNumber("1e-12")),
            AtomicHumanUnit("n", "nano", tt, SciNumber("1e-9")),
            AtomicHumanUnit("μ", "micro", tt, SciNumber.Micro),
            AtomicHumanUnit("m", "milli", tt, SciNumber.Milli),
            AtomicHumanUnit("k", "kilo", tt, SciNumber.Kilo),
            AtomicHumanUnit("M", "mega", tt, SciNumber.Mega),
            AtomicHumanUnit("G", "giga", tt, SciNumber("1e9")),
            AtomicHumanUnit("T", "tera", tt, SciNumber("1e12")),
            AtomicHumanUnit("P", "peta", tt, SciNumber("1e15")),
            AtomicHumanUnit("E", "exa", tt, SciNumber("1e18"))
    )

    private val unitAbbreviations = units.associateBy(AtomicHumanUnit::abbreviation)
    private val prefixAbbreviations = prefixes.associateBy(AtomicHumanUnit::abbreviation)

    fun unitByAbbreviation(s: String) = unitAbbreviations[s]
    fun prefixByAbbreviation(s: String) = prefixAbbreviations[s]

    val abbreviations = unitAbbreviations.keys
}
