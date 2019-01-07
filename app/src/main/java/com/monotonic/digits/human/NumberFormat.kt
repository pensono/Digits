package com.monotonic.digits.human

/**
 * @author Ethan
 */
enum class NumberFormat(val exponentGranularity: Int) {
    SCIENTIFIC(1), ENGINEERING(3)
}