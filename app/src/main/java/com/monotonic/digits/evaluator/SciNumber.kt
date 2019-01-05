package com.monotonic.digits.evaluator

import java.lang.Integer.*
import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.abs

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
sealed class SciNumber {

    /***
     * The number of significant figures, or infinite precision
     */
    abstract val precision: Precision

    /***
     * The number of digits in the number if greater than one, or the negative number of zeroes
     * until the decimal if less than 1
     */
    abstract val magnitude : Int

    abstract operator fun plus(other: SciNumber) : SciNumber
    abstract operator fun minus(other: SciNumber) : SciNumber
    abstract operator fun times(other: SciNumber) : SciNumber
    abstract operator fun div(other: SciNumber) : SciNumber
    abstract operator fun unaryMinus() : SciNumber

    abstract fun pow(n: Int): SciNumber
    abstract fun sqrt() : SciNumber

    abstract fun sin() : SciNumber
    abstract fun cos() : SciNumber
    abstract fun tan() : SciNumber
    abstract fun sinh() : SciNumber
    abstract fun cosh() : SciNumber
    abstract fun tanh() : SciNumber
    abstract fun asin() : SciNumber
    abstract fun acos() : SciNumber
    abstract fun atan() : SciNumber
    abstract fun csc() : SciNumber
    abstract fun sec() : SciNumber
    abstract fun cot() : SciNumber

    abstract fun valueString() : String
    abstract fun valueEqual(other: SciNumber) : Boolean
    abstract fun toDouble() : Double

    companion object {
        val One = Real(1)
        val Zero = Real(0)
    }

    class Real : SciNumber {
        private val backing: BigDecimal

        override val precision: Precision

        override val magnitude: Int
            get() = magnitudeOf(backing.toDouble())

        private fun magnitudeOf(number: Double): Int {
            return if (number == 0.0)
                1
            else
            // Double is not super accurate, but should be good enough
                Math.floor(Math.log10(abs(number))).toInt() + 1
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
                throw IllegalArgumentException("Real(String) cannot be given the empty string")
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
        override operator fun plus(other: SciNumber) = additiveOperation(other, BigDecimal::add)
        override operator fun minus(other: SciNumber) = additiveOperation(other, BigDecimal::minus)
        override operator fun times(other: SciNumber) =
                when (other) {
                    is SciNumber.Real -> Real(backing * other.backing, minPrecision(other))
                    is SciNumber.Nan -> other
                }

        override operator fun div(other: SciNumber) =
                when (other) {
                    is SciNumber.Real -> Real(backing.divide(other.backing, MathContext.DECIMAL128), minPrecision(other))
                    is SciNumber.Nan -> other
                }

        private fun additiveOperation(other: SciNumber, op: (BigDecimal, BigDecimal) -> BigDecimal) : SciNumber =
            when (other) {
                is SciNumber.Nan -> other
                is SciNumber.Real -> {
                    val result = op(backing, other.backing)

                    val newLsd = minLsd(lsd, other.lsd)
                    val newMag = magnitudeOf(result.toDouble())

                    if (newLsd == null) {
                        Real(result, Precision.Infinite)
                    } else {
                        Real(result, Precision.SigFigs(newMag + newLsd))
                    }
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

        private fun minPrecision(other: Real) = minOf(precision, other.precision)

        override operator fun unaryMinus() = Real(-backing, precision)
        operator fun compareTo(other: Real) = backing.compareTo(other.backing)
        override fun pow(n: Int) = Real(backing.pow(n, MathContext.DECIMAL128), precision) // Context needed here?
        override fun sqrt() =
                if (backing >= BigDecimal.ZERO)
                    Real(BigDecimal.valueOf(Math.sqrt(backing.toDouble())), precision)
                else
                    Nan // Until imaginary numbers are implemented
        fun abs() = Real(backing.abs())

        // Condition numbers:
        // https://www.cl.cam.ac.uk/~jrh13/papers/transcendentals.pdf
        override fun sin() = doubleFunction(Math::sin)
        override fun cos() = doubleFunction(Math::cos)
        override fun tan() = doubleFunction(Math::tan)
        override fun sinh() = doubleFunction(Math::sinh)
        override fun cosh() = doubleFunction(Math::cosh)
        override fun tanh() = doubleFunction(Math::tanh)
        override fun asin() = doubleFunction(Math::asin)
        override fun acos() = doubleFunction(Math::acos)
        override fun atan() = doubleFunction(Math::atan)
        override fun csc() = doubleFunction(Math::asin)
        override fun sec() = doubleFunction(Math::acos)
        override fun cot() = doubleFunction(Math::atan)

        private fun doubleFunction(op : (Double) -> Double) : SciNumber {
            val value = op(backing.toDouble())
            return if (value.isNaN()) {
                Nan
            } else {
                Real(BigDecimal(value, MathContext.DECIMAL128), precision)
            }
        }

        override fun equals(other: Any?): Boolean = other is Real && this.backing == other.backing && this.precision == other.precision
        override fun hashCode(): Int = backing.hashCode()
        override fun toString(): String = backing.toPlainString() + " precision: $precision"

        fun reciprocal() = Real(BigDecimal.ONE.divide(backing, MathContext.DECIMAL128), precision)
        override fun toDouble(): Double = backing.toDouble()
        override fun valueEqual(other: SciNumber): Boolean = other is SciNumber.Real && backing.compareTo(other.backing) == 0
        override fun valueString(): String {
            val baseString = backing.toPlainString()
            val sigFigs = baseString.trimStart('0', '.', '-')
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

    object Nan : SciNumber() {
        override val precision: Precision = Precision.Infinite
        override val magnitude: Int = 0 // Not sure what to put here

        override operator fun plus(other: SciNumber) = this
        override operator fun minus(other: SciNumber) = this
        override operator fun times(other: SciNumber) = this
        override operator fun div(other: SciNumber) = this
        override operator fun unaryMinus() = this

        override fun pow(n: Int): SciNumber = this
        override fun sqrt(): SciNumber = this

        override fun sin() = this
        override fun cos() = this
        override fun tan() = this
        override fun sinh() = this
        override fun cosh() = this
        override fun tanh() = this
        override fun asin() = this
        override fun acos() = this
        override fun atan() = this
        override fun csc() = this
        override fun sec() = this
        override fun cot() = this

        override fun valueString(): String = "NaN"
        override fun valueEqual(other: SciNumber): Boolean = other === Nan
        override fun toDouble(): Double = Double.NaN
    }
}