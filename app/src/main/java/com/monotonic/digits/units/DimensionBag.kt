package com.monotonic.digits.units

import com.monotonic.digits.evaluator.SciNumber

class DimensionBag(val values: Map<String, SciNumber.Real>) {

    fun map(op: (SciNumber.Real) -> SciNumber.Real) : DimensionBag = DimensionBag(values.mapValues { (_, v) -> op(v) })

    fun combine(other: DimensionBag, op: (SciNumber.Real, SciNumber.Real) -> SciNumber.Real) : DimensionBag {
        val combinedValues = values.mapValues { (k, v) -> op(other.values.getOrDefault(k, SciNumber.Zero), v) }
            .filterValues { v -> v != SciNumber.Zero }

        return DimensionBag(combinedValues)
    }

    fun magnitude() =
            values.map { it.value.abs() }
                    .reduce { acc, real -> acc.plusReal(real) }
}