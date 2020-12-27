package com.monotonic.digits.human

import com.monotonic.digits.evaluator.ParseResult
import com.monotonic.digits.evaluator.evaluateExpression
import com.monotonic.digits.units.DimensionBag
import com.monotonic.digits.units.NaturalUnit

/**
 * @author Ethan
 */
fun evaluateHumanized(input: String, preferredUnits: Map<DimensionBag, HumanUnit>): ParseResult<HumanQuantity> {
    val parseResult = evaluateExpression(input)
    val usedUnits = largestUsedUnits(input) // Somewhat inefficient to do this if the preferred unit will be used anyways

    return parseResult.invoke {
        val dimensions = it.unit.dimensions
        val preferredUnit = preferredUnits[dimensions] ?: usedUnits[dimensions] ?: bestPositiveMultipleOf(usedUnits, it.unit)
        if (preferredUnit == null) humanize(it) else convert(it, preferredUnit)
    }
}

private fun bestPositiveMultipleOf(usedUnits: Map<DimensionBag, HumanUnit>, unit: NaturalUnit): HumanUnit? {
    // Make sure it's a multiple, and it's a positive multiple
    val factorUnit = usedUnits.values.firstOrNull { it.isMultiple(unit) && unit / it > 0 } ?: return null
    return factorUnit * (unit / factorUnit)
}
