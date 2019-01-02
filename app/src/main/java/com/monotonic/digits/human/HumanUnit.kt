package com.monotonic.digits.human

import com.monotonic.digits.evaluator.SciNumber
import com.monotonic.digits.evaluator.Quantity
import com.monotonic.digits.prettyExponent
import com.monotonic.digits.units.NaturalUnit
import com.monotonic.digits.units.PrefixUnit
import com.monotonic.digits.units.UnitSystem
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

    fun incorperateUnit(other: AtomicHumanUnit, exponent: Int): HumanUnit {
        val newMap = HashMap(components)
        newMap[other] = newMap.getOrDefault(other, 0) + exponent
        return HumanUnit(newMap, prefix)
    }

    override fun equals(other: Any?) =
            other is HumanUnit && other.components == components && other.prefix == prefix

    override fun hashCode(): Int = components.hashCode()

    override fun toString(): String = "HumanUnit($components, $abbreviation)"
}

val humanizationCache = mutableMapOf<Map<String, Int>, HumanUnit>()

/**
 * Returns an inverse unit that can be used to normalize the result
 */
fun humanize(quantity: Quantity) : HumanQuantity {
    if (quantity.unit.dimensionallyEqual(UnitSystem.void) || quantity.unit.dimensionMagnitude >= 100) { // Just give up for huge units
        return HumanQuantity(quantity.value * quantity.unit.factor, HumanUnit(mapOf()))
    }

    val prefixMagnitude =
        if (quantity.normalizedValue.valueEqual(SciNumber.Zero))
            quantity.unit.factor.magnitude
        else
            quantity.normalizedValue.magnitude

    // Use the one to round up and avoid 0 digits in the front (like in .123m)
    val prefixIndex = (prefixMagnitude - UnitSystem.prefixMagStart - 1) / 3
    val prefixExponent = (prefixIndex * 3) + UnitSystem.prefixMagStart
    val prefixFactor = SciNumber.Real(10).pow(prefixExponent)
    val prefixUnit = UnitSystem.prefixes[prefixIndex]

    val humanizedValue = quantity.value * quantity.unit.factor / prefixFactor

    val cacheLookup = humanizationCache[quantity.unit.dimensions]
    if (cacheLookup != null) {
        return HumanQuantity(humanizedValue, cacheLookup.withPrefix(prefixUnit))
    }

    val visitedUnits = mutableSetOf<HumanUnit>()
    val comparator = compareBy<HumanUnit> { (it - quantity.unit).dimensionMagnitude }
            .thenBy { it.dimensions.size }
            .thenBy(HumanUnit::exponentMagnitude)
    val visitQueue = PriorityQueue<HumanUnit>(comparator)

    visitQueue.add(HumanUnit(mapOf())) // No prefix

    while (visitQueue.isNotEmpty()) {
        val currentUnit = visitQueue.poll()
        visitedUnits.add(currentUnit)

        // I'm not 100% on the fact that this temporarily creates a human unit that has atomic units with degree 0
        val possibleNewUnits = UnitSystem.units.map(currentUnit::plus) + UnitSystem.units.map(currentUnit::minus)
        val newUnits = possibleNewUnits.filter{ !visitedUnits.contains(it) && !it.components.values.contains(0) }

        val correctUnit = newUnits.firstOrNull { unit -> unit.dimensionallyEqual(quantity.unit) }
        if (correctUnit != null) {
            humanizationCache[quantity.unit.dimensions] = correctUnit
            return HumanQuantity(humanizedValue, correctUnit.withPrefix(prefixUnit))
        }

        visitQueue.addAll(newUnits)
    }

    // This code should never really execute because there should be a base unit for each dimension (like meters or seconds)
    val extraUnit = AtomicHumanUnit("Unk", "Unknown", null, quantity.unit.dimensions, quantity.unit.factor)
    return HumanQuantity(humanizedValue, HumanUnit(mapOf(extraUnit to 1)))
}

/**
 *
 * @param quantity must be dimensionally equal to unit
 */
fun convert(quantity: Quantity, unit: HumanUnit) : HumanQuantity =
    HumanQuantity(quantity.value * (quantity.unit.factor / unit.prefix.factor), unit)
