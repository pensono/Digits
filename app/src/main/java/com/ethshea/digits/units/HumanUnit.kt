package com.ethshea.digits.units

import com.ethshea.digits.SciNumber
import kotlin.math.absoluteValue

/**
 * @author Ethan
 */

class HumanUnit(val components: Map<AtomicHumanUnit, Int>, prefix: PrefixUnit = PrefixUnit.One) : NaturalUnit() {
    val underlyingUnit =
            components.map { entry -> entry.key * entry.value }
                    .fold(UnitSystem.void, NaturalUnit::plus)

    override val dimensions = underlyingUnit.dimensions
    override val factor = underlyingUnit.factor

    val exponentMagnitude = components.values.map{ it.absoluteValue }.sum()

    val abbreviation = prefix.abbreviation + components.map { entry -> entry.key.abbreviation + prettyExponent(entry.value) }.joinToString("")

    operator fun plus(other: AtomicHumanUnit) = incorperateUnit(other, 1)
    operator fun minus(other: AtomicHumanUnit) = incorperateUnit(other, -1)

    fun withPrefix(prefix: PrefixUnit) = HumanUnit(components, prefix)

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