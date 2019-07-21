package com.monotonic.digits.human

import com.monotonic.digits.evaluator.ParseResult
import com.monotonic.digits.evaluator.evaluateExpression
import com.monotonic.digits.units.NaturalUnit

/**
 * @author Ethan
 */
fun evaluateHumanized(input: String, preferredUnits: Map<Map<String, Int>, HumanUnit>) : ParseResult<HumanQuantity> {
    val parseResult = evaluateExpression(input)
    val usedUnits = largestUsedUnits(input) // Somewhat inefficient to do this if the preferred unit will be used anyways

    return parseResult.invoke {
        val dimensions = it.unit.dimensions
        val preferredUnit = preferredUnits[dimensions] ?: usedUnits[dimensions] ?: bestMultipleOf(usedUnits, it.unit)
        if (preferredUnit == null) humanize(it) else convert(it, preferredUnit)
    }
}

private fun bestMultipleOf(usedUnits: Map<Map<String, Int>, HumanUnit>, unit: NaturalUnit): HumanUnit? {
    val factor = usedUnits.values.firstOrNull { it.isMultiple(unit) } ?: return null
    return factor * (unit / factor)
}
