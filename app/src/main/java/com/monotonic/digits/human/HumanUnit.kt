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
    companion object {
        val Void = HumanUnit(mapOf())
    }

    val underlyingUnit =
            components.map { entry -> entry.key * entry.value }
                    .fold(UnitSystem.void, NaturalUnit::plus)

    override val dimensions = underlyingUnit.dimensions
    override val factor = underlyingUnit.factor

    val exponentMagnitude = components.values.map{ it.absoluteValue }.sum()

    val abbreviation : String
        get() {
            val splitComponents = components.entries.partition { it.value > 0 }
            val numerator = splitComponents.first.map { entry -> entry.key.abbreviation + prettyExponent(entry.value) }.joinToString("")
            val denominator = splitComponents.second.map { entry -> entry.key.abbreviation + prettyExponent(-entry.value) }.joinToString("")

            val completeNumerator = if (numerator.isEmpty()) "1" else numerator
            val completeDenominator = if (denominator.isEmpty()) "" else "/$denominator"

            return if (numerator.isEmpty() && denominator.isEmpty())
                prefix.abbreviation
            else
                prefix.abbreviation + completeNumerator + completeDenominator
        }

    operator fun plus(other: AtomicHumanUnit) = incorperateUnit(other, 1)
    operator fun minus(other: AtomicHumanUnit) = incorperateUnit(other, -1)
    override operator fun times(n : Int) = HumanUnit(components.mapValues { e -> e.value * n }, prefix * n)

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
        if (quantity.value.valueEqual(SciNumber.Zero))
            quantity.unit.factor.magnitude
        else
            quantity.normalizedValue.magnitude

    val cacheLookup = humanizationCache[quantity.unit.dimensions]
    if (cacheLookup != null) {
        return applyPrefix(quantity, cacheLookup, prefixMagnitude)
    }

    val visitedUnits = mutableSetOf<HumanUnit>()
    val comparator = compareBy<HumanUnit> { (it - quantity.unit).dimensionMagnitude }
            .thenBy { it.dimensions.size }
            .thenBy(HumanUnit::exponentMagnitude)
    val visitQueue = PriorityQueue(comparator)

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
            return applyPrefix(quantity, correctUnit, prefixMagnitude)
        }

        visitQueue.addAll(newUnits)
    }

    // This code should never execute because there should be a base unit for each dimension (like meters or seconds). We'll do something sensible anyways
    val extraUnit = AtomicHumanUnit("Unk", "Unknown", null, quantity.unit.dimensions, quantity.unit.factor)
    return applyPrefix(quantity, HumanUnit(mapOf(extraUnit to 1)), prefixMagnitude)
}

private fun applyPrefix(quantity: Quantity, unit: HumanUnit, prefixMagnitude: Int): HumanQuantity {
    // Use the one to round up and avoid 0 digits in the front (like in .123m)
    // Also ignore the centi- prefix
    val unscaledPrefix = UnitSystem.prefixes.first { p -> p.exponent <= prefixMagnitude - 1 && p.exponent % 3 == 0}
    val factor = SciNumber.Real(10).pow(unscaledPrefix.exponent)

    val prefix =
            if (unit.components.size == 1) {
                val nameScale = unit.components.values.first() // So that we can fake (km)2 rather than k(m^2)
                val scaledUnit = UnitSystem.prefixes.first { p -> p.exponent <= ((prefixMagnitude - 1) / nameScale) && p.exponent % 3 == 0}
                PrefixUnit(scaledUnit.abbreviation, scaledUnit.name, unscaledPrefix.exponent, "")
            } else {
                unscaledPrefix
            }

    val humanizedValue = quantity.value * (quantity.unit.factor / factor) / unit.factor
    return HumanQuantity(humanizedValue, unit.withPrefix(prefix))
}


/**
 *
 * @param quantity must be dimensionally equal to unit
 */
fun convert(quantity: Quantity, unit: HumanUnit) : HumanQuantity =
    HumanQuantity(quantity.value * quantity.unit.factor / (unit.prefix.factor * unit.factor), unit)
