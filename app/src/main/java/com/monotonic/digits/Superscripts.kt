package com.monotonic.digits

import kotlin.math.absoluteValue

val superscriptMap = listOf('⁰', '¹', '²', '³', '⁴', '⁵', '⁶', '⁷', '⁸', '⁹')
val superscriptMinus = '⁻'

fun prettyExponent(number: Int): String {
    if (number == 1) {
        return ""
    }

    val stringRepresentation = number.absoluteValue.toString()
    val superscript = stringRepresentation.map { char -> superscriptMap[char - '0'] }.joinToString("")
    val sign = if (number < 0) superscriptMinus.toString() else ""

    return sign + superscript
}

/**
 * Turn any superscripts in the input into regular digits
 */
fun unsuperscript(input: String): String {
    var result = input
    for (i in 0 until 10) {
        result = result.replace(superscriptMap[i], '0' + i)
    }
    return result.replace(superscriptMinus, '-')
}

/**
 * Parse a number which could contain superscripts
 */
fun parseNumber(input: String) = Integer.parseInt(unsuperscript(input))

/**
 * True if a number is a digit or a superscript or a negative sign
 */
fun isNumber(input: Char) = input.isDigit() || superscriptMap.contains(input) || input == '-' || input == superscriptMinus