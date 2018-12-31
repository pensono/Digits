package com.ethshea.digits.human

import com.ethshea.digits.evaluator.TokenIterator
import com.ethshea.digits.isNumber
import com.ethshea.digits.parseNumber
import com.ethshea.digits.units.UnitSystem
import org.antlr.v4.runtime.misc.Interval

fun parseHumanUnit(input: String) : HumanUnit? {
    val tokens = TokenIterator(input, Interval(0, input.length))
    var unit = HumanUnit(mapOf())

    val doubleUnits = UnitSystem.prefixAbbreviations.keys.intersect(UnitSystem.unitAbbreviations.keys)

    val prefix = tokens.nextLargest(UnitSystem.prefixAbbreviations)
    if (prefix != null) {
        val beginning = input.substring(prefix.abbreviation.length).split("/")[0]
        if (doubleUnits.contains(prefix.abbreviation) && (beginning.isEmpty() || !beginning[0].isLetter())) { // Special case for mili which conflicts with meters
            val exponentStr = tokens.nextWhile(::isNumber)
            val exponent = if (exponentStr == "") 1 else parseNumber(exponentStr)

            unit = unit.incorperateUnit(UnitSystem.unitAbbreviations[prefix.abbreviation]!!, exponent)
        } else {
            unit = HumanUnit(mapOf(), prefix)

            if (!tokens.hasNext()) {
                return null // units need to have more than just a prefix
            }
        }
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
    return unit
}