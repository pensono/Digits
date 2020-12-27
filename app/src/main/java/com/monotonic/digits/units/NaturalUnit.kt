package com.monotonic.digits.units

import com.monotonic.digits.evaluator.Precision
import com.monotonic.digits.evaluator.SciNumber


/**
 * @param factor must be greater than zero
 * @author Ethan
 */
open class NaturalUnit constructor(open val dimensions: DimensionBag, open val factor: SciNumber.Real) {
    // These two constructors are confusing, should be addressed
    constructor(dimensions: Map<String, Int> = mapOf(), factor: String = "1") : this(DimensionBag(dimensions), SciNumber.Real(factor, Precision.Infinite))

    operator fun plus(other: NaturalUnit) =
            NaturalUnit(dimensions.combine(other.dimensions, Int::plus), (factor * other.factor) as SciNumber.Real) // real * real always = real

    operator fun minus(other: NaturalUnit) =
            NaturalUnit(dimensions.combine(other.dimensions, Int::minus), (factor / other.factor) as SciNumber.Real)

    open operator fun times(n: Int): NaturalUnit =
            NaturalUnit(dimensions.map { it * n }, factor.powReal(SciNumber.Real(n)))

    operator fun unaryMinus() =
            NaturalUnit(dimensions.map { -it }, factor.reciprocal())

    fun half() =
            NaturalUnit(dimensions.map { it / 2 }, factor.sqrt() as SciNumber.Real)

    /**
     * Result only makes sense iff isMultiple(other)
     * TODO Should also examine the factors
     */
    operator fun div(other: NaturalUnit): Int =
            dimensions.values.map { d -> d.value / other.dimensions.values.getOrDefault(d.key, 0) }.first()

    override fun equals(other: Any?) =
            other is NaturalUnit && representationEqual(other)

    /***
     * @return true iff all unit exponents are even
     */
    fun isEven() = dimensions.values.all { it.value % 2 == 0 }

    fun representationEqual(other: NaturalUnit) = dimensionallyEqual(other) && factor == other.factor
    fun dimensionallyEqual(other: NaturalUnit) = dimensions == other.dimensions

    // TODO should also examine the factors
    // Should this exist in DimensionBag?
    fun isMultiple(other: NaturalUnit) = dimensions.values.keys == other.dimensions.values.keys
            && dimensions.values.all { d -> other.dimensions.values.getOrDefault(d.key, 0) % d.value == 0 }
            && dimensions.values.map { d -> d.value / other.dimensions.values.getOrDefault(d.key, 0) }.distinct().count() <= 1


    override fun hashCode() = dimensions.hashCode() xor factor.hashCode()
    override fun toString() = "NaturalUnit(dimensions: $dimensions, factor: $factor)"
}