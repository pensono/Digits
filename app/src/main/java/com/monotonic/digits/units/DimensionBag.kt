package com.monotonic.digits.units

import kotlin.math.absoluteValue

class DimensionBag(val values: Map<String, Int>) {
    constructor(vararg values: Pair<String, Int>) : this(mapOf(*values))

    fun map(op: (Int) -> Int): DimensionBag = DimensionBag(values.mapValues { (_, v) -> op(v) })

    fun combine(other: DimensionBag, op: (Int, Int) -> Int): DimensionBag {
        val combinedValues = (values + other.values).keys
                .associateWith { k -> op(values.getOrDefault(k, 0), other.values.getOrDefault(k, 0)) }
                .filterValues { v -> v != 0 }

        return DimensionBag(combinedValues)
    }

    fun magnitude() = values.map { it.value.absoluteValue }.reduce(Int::plus)

    override fun toString(): String = values.entries.joinToString(" ") { (k, v) -> "${k}:${v}" }

    override fun equals(other: Any?): Boolean = other is DimensionBag && values == other.values

    override fun hashCode(): Int = values.hashCode()
}