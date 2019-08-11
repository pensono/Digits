package com.monotonic.digits.human

import com.monotonic.digits.evaluator.TokenIterator
import com.monotonic.digits.isNumber
import com.monotonic.digits.parseNumber
import com.monotonic.digits.units.PrefixUnit
import com.monotonic.digits.units.UnitSystem
import org.antlr.v4.runtime.misc.Interval

fun parseHumanUnit(input: String) : HumanUnit? {
    val tokens = TokenIterator(input, Interval(0, input.length))
    var unit = HumanUnit(mapOf())

    // Prefer the prefixes
    val allAbbreviations = UnitSystem.unitAbbreviations.toMutableMap()
    for ((key, value) in UnitSystem.prefixAbbreviations) {
        allAbbreviations[key] = value
    }
    val doubleUnits = UnitSystem.prefixAbbreviations.keys.intersect(UnitSystem.unitAbbreviations.keys)

    val first = tokens.nextLargest(allAbbreviations)
    if (first != null) {
        // If it was just a prefix which overlaps with a unit, then use the unit
        // We want the rest of the numerator string to either be empty or only contain numbers
        val numerator = tokens.peekRest().split("/")[0]
        if (doubleUnits.contains(first.abbreviation) && numerator.all(::isNumber)) {
            val exponentStr = tokens.nextWhile(::isNumber)
            val exponent = if (exponentStr == "") 1 else parseNumber(exponentStr)
            unit = HumanUnit(mapOf(UnitSystem.unitAbbreviations[first.abbreviation]!! to exponent))
        } else if (first is PrefixUnit){
            unit = HumanUnit(mapOf(), first)

            if (!tokens.hasNext()) {
                return null // units need to have more than just a prefix
            }
        } else { // Regular unit appeared first
            val exponentStr = tokens.nextWhile(::isNumber)
            val exponent = if (exponentStr == "") 1 else parseNumber(exponentStr)
            unit = unit.incorperateUnit(first, exponent)
        }
    } else {
        return null // Invalid unit
    }

    var invert = false
    while (tokens.hasNext()) {
        val newUnit = tokens.nextLargest(UnitSystem.unitAbbreviations)
        if (newUnit != null) {
            val sign = if (invert) -1 else 1
            val exponentStr = tokens.nextWhile(::isNumber)
            val exponent = if (exponentStr == "") 1 else parseNumber(exponentStr)

            unit = unit.incorperateUnit(newUnit, exponent * sign)
        } else if (tokens.isNext("/")) {
            invert = true
            tokens.consume("/")
        } else {
            return null
        }
    }

    if (unit.components.size == 1) {
        val exponent = unit.components.values.first()
        val prefix = unit.prefix
        unit = unit.withPrefix(PrefixUnit(prefix.abbreviation, prefix.name, prefix.exponent * exponent, ""))
    }

    return unit
}