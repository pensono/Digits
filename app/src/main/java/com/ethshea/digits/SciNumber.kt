package com.ethshea.digits

import java.lang.Integer.*
import java.math.BigDecimal
import java.math.MathContext

sealed class Precision : Comparable<Precision> {
    abstract operator fun plus(value: Int) : Precision
    abstract operator fun minus(value: Int) : Precision

    object Infinite : Precision() {
        override fun compareTo(other: Precision): Int =
                when (other) {
                    Infinite -> 0
                    else -> 1
                }

        override fun toString(): String = "Infinite"
        override operator fun plus(value: Int) = this
        override operator fun minus(value: Int) = this
    }

    data class SigFigs(val amount: Int) : Precision() {
        override fun compareTo(other: Precision): Int =
                when (other) {
                    is SigFigs -> compare(amount, other.amount) // Might be backwards
                    else -> -1  // Infinite
                }

        override operator fun plus(value: Int) = SigFigs(amount + value)
        override operator fun minus(value: Int) = SigFigs(amount - value)
    }
}

/**
 * @author Ethan
 */
class SciNumber {
    companion object {
        val One = SciNumber(1)
        val Zero = SciNumber(0)
    }

    private val backing: BigDecimal

    /***
     * The number of significant figures, or infinite precision
     */
    val precision: Precision

    /***
     * The number of digits in the number if greater than one, or the negative number of zeroes
     * until the decimal if less than 1
     */
    val magnitude: Int
        get() = magnitudeOf(backing)

    private fun magnitudeOf(number: BigDecimal): Int {
        return if (number.compareTo(BigDecimal.ZERO) == 0)
            1
        else
        // Double is not super accurate, but should be good enough
            Math.floor(Math.log10(number.abs().toDouble())).toInt() + 1
    }

    val lsd  : Int?
        get() =
            when (precision) {
                is Precision.Infinite -> null
                is Precision.SigFigs -> precision.amount - magnitude
            }

    /***
     * @param value A non-empty string containing only digits
     */
    constructor(value: String) {
        // We can't just utilize the BigDecimal constructor because
        // "150." and "150" parse to the same thing, when they should have
        // different amounts of precision (3 vs 2 digits)

        if (value.isEmpty()) {
            throw IllegalArgumentException("SciNumber(String) cannot be given the empty string")
        }

        // Algorithm based on concise rules: https://en.wikipedia.org/wiki/Significant_figures
        val noLeadingZeroes = value.trimStart('0', '-')
        when {
            noLeadingZeroes.trim('0', '.').isEmpty() -> { // Only 0s
                backing = BigDecimal.ZERO
                precision = Precision.Infinite
            }
            noLeadingZeroes.contains('.') -> {
                // All trailing zeroes are significant
                backing = BigDecimal(value)
                precision = Precision.SigFigs(backing.precision())
            }
            else -> {
                // Handle the ambiguity of trailing zeroes by always treating zeroes as significant
                backing = BigDecimal(value)
                precision = Precision.SigFigs(noLeadingZeroes.length)
            }
        }
    }

    // Should be considered an infinite number of sigfigs, they are exact quantities (like 10 or 2)
    constructor(value: Int) : this(BigDecimal(value, MathContext.DECIMAL128), Precision.Infinite)
    constructor(value: Double) : this(BigDecimal(value, MathContext.DECIMAL128))
    constructor(value: Double, sigFigs: Int) : this(BigDecimal(value, MathContext.DECIMAL128), Precision.SigFigs(sigFigs))

    constructor(value: String, precision: Precision) : this(BigDecimal(value), precision)

    private constructor(value: BigDecimal) : this(value, Precision.SigFigs(value.precision()))

    private constructor(value: BigDecimal, precision: Precision) {
        backing = value
        this.precision = precision
    }

    // Precision based on algorithm described in https://en.wikipedia.org/wiki/Significant_figures#Arithmetic
    operator fun plus(other: SciNumber) = additiveOperation(other, BigDecimal::add)
    operator fun minus(other: SciNumber) = additiveOperation(other, BigDecimal::minus)
    operator fun times(other: SciNumber) = SciNumber(backing * other.backing, minPrecision(other))
    operator fun div(other: SciNumber) = SciNumber(backing.divide(other.backing, MathContext.DECIMAL128), minPrecision(other))

    private fun additiveOperation(other: SciNumber, op: (BigDecimal, BigDecimal) -> BigDecimal) : SciNumber {
        val result = op(backing, other.backing)

        val newLsd = minLsd(lsd, other.lsd)
        val newMag = magnitudeOf(result)

        return if (newLsd == null) {
            SciNumber(result, Precision.Infinite)
        } else {
            SciNumber(result, Precision.SigFigs(newMag + newLsd))
        }
    }

    private fun minLsd(lsdA: Int?, lsdB: Int?): Int? =
        if (lsdA == null) {
            lsdB
        } else {
            if (lsdB == null) {
                lsdA
            } else {
                min(lsdA, lsdB)
            }
        }

    private fun minPrecision(other: SciNumber) = minOf(precision, other.precision)

    operator fun unaryMinus() = SciNumber(-backing, precision)
    operator fun compareTo(other: SciNumber) = backing.compareTo(other.backing)
    fun pow(n: Int) = SciNumber(backing.pow(n, MathContext.DECIMAL128), precision) // Context needed here?
    fun abs() = SciNumber(backing.abs())

    fun sin() = doubleFunction(Math::sin)
    fun cos() = doubleFunction(Math::cos)
    fun tan() = doubleFunction(Math::tan)
    fun sinh() = doubleFunction(Math::sinh)
    fun cosh() = doubleFunction(Math::cosh)
    fun tanh() = doubleFunction(Math::tanh)
    fun asin() = doubleFunction(Math::asin)
    fun acos() = doubleFunction(Math::acos)
    fun atan() = doubleFunction(Math::atan)

    private fun doubleFunction(op : (Double) -> Double) = SciNumber(BigDecimal(op(backing.toDouble()), MathContext.DECIMAL128), precision)

    override fun equals(other: Any?): Boolean = other is SciNumber && this.backing == other.backing && this.precision == other.precision
    override fun hashCode(): Int = backing.hashCode()
    override fun toString(): String = backing.toPlainString() + " precision: $precision"

    fun reciprocal() = SciNumber(BigDecimal.ONE.divide(backing, MathContext.DECIMAL128), precision)
    fun toDouble(): Double = backing.toDouble()
    fun valueEqual(other: SciNumber): Boolean = backing.compareTo(other.backing) == 0
    fun valueString(): String {
        val baseString = backing.toPlainString()
        val sigFigs = baseString.trimStart('0','.')
                .count { it.isDigit() }

        return when (precision) {
            is Precision.Infinite -> baseString
            is Precision.SigFigs -> {
                val missing = max(0, precision.amount - sigFigs)
                val decimalStr = if (!baseString.contains('.') && missing > 0) "." else ""
                baseString + decimalStr + "0".repeat(missing)
            }
        }
    }
}