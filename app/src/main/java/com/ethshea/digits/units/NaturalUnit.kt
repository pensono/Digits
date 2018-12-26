package com.ethshea.digits.units

import com.ethshea.digits.evaluator.Precision
import com.ethshea.digits.evaluator.SciNumber
import kotlin.math.absoluteValue


/**
 * @author Ethan
 */
open class NaturalUnit constructor(open val dimensions: Map<String, Int>, open val factor: SciNumber) {
    constructor(dimensions: Map<String, Int> = mapOf(), factor: String = "1") : this(dimensions, SciNumber(factor, Precision.Infinite))

     val dimensionMagnitude : Int
         get() = dimensions.map { it.value.absoluteValue }.sum()

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
    override fun toString() = "NaturalUnit(dimensions: $dimensions, factor: $factor)"

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