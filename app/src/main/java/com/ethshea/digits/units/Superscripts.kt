package com.ethshea.digits.units

import kotlin.math.absoluteValue

val superscriptMap = listOf('⁰', '¹', '²', '³', '⁴', '⁵', '⁶', '⁷', '⁸', '⁹')
val superscriptMinus = "⁻"

fun prettyExponent(number: Int) : String {
    if (number == 1) {
        return ""
    }

    val stringRepresentation = number.absoluteValue.toString()
    val superscript = stringRepresentation.map { char -> superscriptMap[char - '0'] }.joinToString("")
    val sign = if (number < 0) superscriptMinus else ""

    return sign + superscript
}

/**
 * Turn any superscripts in the input into regular digits
 */
fun unsuperscript(input: String) : String {
    var result = input
    for (i in 0..9) {
        result = result.replace(superscriptMap[i], '0' + i)
    }
    return result
}

/**
 * Parse a number which could contain superscripts
 */
fun parseNumber(input: String) = Integer.parseInt(unsuperscript(input))

/**
 * True if a number is a digit or a superscript
 */
fun isNumber(input: Char) = input.isDigit() || superscriptMap.contains(input)