package com.ethshea.digits.units

import com.ethshea.digits.evaluator.Precision
import com.ethshea.digits.evaluator.SciNumber
import kotlin.math.absoluteValue


/**
 * @param factor must be greater than zero
 * @author Ethan
 */
open class NaturalUnit constructor(open val dimensions: Map<String, Int>, open val factor: SciNumber.Real) {
    constructor(dimensions: Map<String, Int> = mapOf(), factor: String = "1") : this(dimensions, SciNumber.Real(factor, Precision.Infinite))

     val dimensionMagnitude : Int
         get() = dimensions.map { it.value.absoluteValue }.sum()

    operator fun plus(other: NaturalUnit) =
            NaturalUnit(combineMapsDefault(dimensions, other.dimensions, Int::plus), (factor * other.factor) as SciNumber.Real) // real * real always = real

    operator fun minus(other: NaturalUnit) =
            NaturalUnit(combineMapsDefault(dimensions, other.dimensions, Int::minus), (factor / other.factor) as SciNumber.Real)

    operator fun times(n: Int) : NaturalUnit =
            NaturalUnit(dimensions.mapValues { entry -> entry.value * n }, factor.pow(n))

    operator fun unaryMinus() =
            NaturalUnit(dimensions.mapValues { e -> -e.value }, factor.reciprocal())

    fun half() =
            NaturalUnit(dimensions.mapValues { entry -> entry.value / 2 }, factor.sqrt() as SciNumber.Real)

    /***
     * @return true iff all unit exponents are even
     */
    fun isEven() = dimensions.all { it.value % 2 == 0 }

    override fun equals(other: Any?) =
            other is NaturalUnit && representationEqual(other)

    fun representationEqual(other: NaturalUnit) = dimensionallyEqual(other) && factor == other.factor
    fun dimensionallyEqual(other: NaturalUnit) = dimensions == other.dimensions

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
}