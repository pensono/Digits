package com.ethshea.digits.evaluator

import com.ethshea.digits.NaturalUnit
import com.ethshea.digits.UnitSystem
import java.math.BigDecimal
import java.util.*

/**
 * @author Ethan
 */

sealed class ParseResult {
    class Success(var root: Any) : ParseResult()
    class Failure(var message: String, val location: Int)
}

fun parseInput(input: String) : Quantity? {
    return parseInput(TokenIterator(input))
}

fun parseInput(tokens: TokenIterator) : Quantity? {
    return null
}

fun parseNumeric(tokens: TokenIterator) : Quantity? {
    var value = ""
    while (tokens.hasNext() && (tokens.peek().isDigit() || tokens.peek() == '.')) {
        value += tokens.next()
    }

    val unit = parseUnit(tokens)

    return try {
        Quantity(BigDecimal(value), unit)
    } catch (e: NumberFormatException) {
        null
    }
}

fun parseUnit(tokens: TokenIterator) : NaturalUnit? {
    var invert = false
    var unit = UnitSystem.void
    var foundUnit = false

    outer@ while (tokens.hasNext()) {
        for (abbreviation in UnitSystem.abbreviations.sortedBy { -it.length }) {
            if (tokens.isNext(abbreviation)) {
                val newUnit = UnitSystem.byAbbreviation(abbreviation)!!
                unit += if (invert) -newUnit else newUnit
                tokens.consume(abbreviation)
                foundUnit = true
                continue@outer
            }
        }

        if (tokens.isNext("/")) {
            invert = true
            tokens.consume("/")
        } else {
            return null
        }
    }

    return if (foundUnit) unit else null
}