package com.monotonic.digits.units

import com.monotonic.digits.evaluator.Precision
import com.monotonic.digits.evaluator.SciNumber
import kotlin.math.absoluteValue


/**
 * @param factor must be greater than zero
 * @author Ethan
 */
open class NaturalUnit constructor(open val dimensions: DimensionBag, open val factor: SciNumber.Real) {
    constructor(dimensions: Map<String, SciNumber.Real> = mapOf(), factor: String = "1") : this(DimensionBag(dimensions), SciNumber.Real(factor, Precision.Infinite))

    operator fun plus(other: NaturalUnit) =
            NaturalUnit(dimensions.combine(other.dimensions) { a, b -> a.plusReal(b) }, (factor * other.factor) as SciNumber.Real) // real * real always = real

    operator fun minus(other: NaturalUnit) =
            NaturalUnit(dimensions.combine(other.dimensions) { a, b -> a.minusReal(b) }, (factor / other.factor) as SciNumber.Real)

    open operator fun times(n: SciNumber.Real) : NaturalUnit =
            NaturalUnit(dimensions.map { it.timesReal(n) }, factor.pow(n))

    operator fun unaryMinus() =
            NaturalUnit(dimensions.map { -it }, factor.reciprocal())

    fun half() =
            NaturalUnit(dimensions.map { it.divReal(SciNumber.Real(2)) }, factor.sqrt() as SciNumber.Real)

    /**
     * Result only makes sense iff isMultiple(other)
     */
    operator fun div(other: NaturalUnit) : SciNumber.Real =
            dimensions.values.map { d -> d.value.divReal(other.dimensions.values.getOrDefault(d.key, SciNumber.Zero)) }.first()

    override fun equals(other: Any?) =
            other is NaturalUnit && representationEqual(other)

    fun representationEqual(other: NaturalUnit) = dimensionallyEqual(other) && factor == other.factor
    fun dimensionallyEqual(other: NaturalUnit) = dimensions == other.dimensions

    fun isMultiple(other: NaturalUnit) = dimensions.keys == other.dimensions.keys
            && dimensions.all { d -> other.dimensions.getOrDefault(d.key, 0) % d.value == 0 }
            && dimensions.map { d -> d.value / other.dimensions.getOrDefault(d.key, 0) }.distinct().count() <= 1


    override fun hashCode() = dimensions.hashCode() xor factor.hashCode()
    override fun toString() = "NaturalUnit(dimensions: $dimensions, factor: $factor)"
}