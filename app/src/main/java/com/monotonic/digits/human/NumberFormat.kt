package com.monotonic.digits.human

/**
 * @author Ethan
 * @param exponentGranularity Exponents of numbers in this format must be multiples of this number
 * @param minimumExponent The smallest exponent magnitude which is written out
 */
enum class NumberFormat(val exponentGranularity: Int, val minimumExponent: Int) {
    SCIENTIFIC(1, 1),
    ENGINEERING(3, 6)
}