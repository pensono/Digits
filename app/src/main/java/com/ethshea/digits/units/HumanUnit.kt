package com.ethshea.digits.units

import com.ethshea.digits.SciNumber
import com.ethshea.digits.evaluator.HumanQuantity
import com.ethshea.digits.evaluator.Quantity
import com.ethshea.digits.prettyExponent
import java.util.*
import kotlin.math.absoluteValue

/**
 * @author Ethan
 */

class HumanUnit(val components: Map<AtomicHumanUnit, Int>, val prefix: PrefixUnit = PrefixUnit.One) : NaturalUnit() {
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

val prefixMagStart = -15
val prefixes = listOf("f", "p", "n", "μ", "m", "", "k", "M", "G", "T") // TODO make this not exist by using UnitSystem instead

/**
 * Returns an inverse unit that can be used to normalize the result
 */
fun humanize(quantity: Quantity) : HumanQuantity {
    if (quantity.unit.dimensionallyEqual(UnitSystem.void)) {
        return HumanQuantity(quantity.value * quantity.unit.factor, HumanUnit(mapOf()))
    }

    val prefixMagnitude = quantity.magnitude
    val prefixIndex = (prefixMagnitude - prefixMagStart) / 3
    val prefixExponent = (prefixIndex * 3) + prefixMagStart
    val prefixFactor = SciNumber(10).pow(prefixExponent)
    val prefixUnit = PrefixUnit(prefixes[prefixIndex], "prefix", factor = prefixFactor)

    val humanizedValue = quantity.value * quantity.unit.factor / prefixFactor

    val visitedUnits = mutableSetOf<HumanUnit>()
    val visitQueue = PriorityQueue<HumanUnit>(compareBy(HumanUnit::exponentMagnitude))

    visitQueue.add(HumanUnit(mapOf())) // No prefix

    while (visitQueue.isNotEmpty()) {
        val currentUnit = visitQueue.poll()
        visitedUnits.add(currentUnit)

        // I'm not 100% on the fact that this temporarily creates a human unit that has atomic units with degree 0
        val possibleNewUnits = UnitSystem.units.map(currentUnit::plus) + UnitSystem.units.map(currentUnit::minus)
        val newUnits = possibleNewUnits.filter{ !visitedUnits.contains(it) && !it.components.values.contains(0) }

        val correctUnit = newUnits.firstOrNull { unit -> unit.dimensionallyEqual(quantity.unit) }
        if (correctUnit != null) {
            return HumanQuantity(humanizedValue, correctUnit.withPrefix(prefixUnit))
        }

        visitQueue.addAll(newUnits)
    }

    // This code should never really execute because there should be a base unit for each dimension (like meters or seconds)
    val extraUnit = AtomicHumanUnit("Unk", "Unknown", quantity.unit.dimensions, quantity.unit.factor)
    return HumanQuantity(humanizedValue, HumanUnit(mapOf(extraUnit to 1)))
}