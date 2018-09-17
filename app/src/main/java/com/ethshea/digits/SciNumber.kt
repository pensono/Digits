package com.ethshea.digits

import java.math.BigDecimal
import java.math.MathContext

/**
 * @author Ethan
 */
class SciNumber {
    companion object {
        val One = SciNumber("1")
        val Zero = SciNumber("0")

        val Kilo = SciNumber("1e3")
        val Mega = SciNumber("1e6")
        val Milli = SciNumber("1e-3")
        val Micro = SciNumber("1e-6")
    }

    private val backing: BigDecimal

    constructor(value: String) : this(BigDecimal(value, MathContext.DECIMAL128))
    constructor(value: Int) : this(BigDecimal(value, MathContext.DECIMAL128))
    constructor(value: Double) : this(BigDecimal(value, MathContext.DECIMAL128))

    private constructor(value: BigDecimal) {
        backing = value
    }

    operator fun plus(other: SciNumber) = SciNumber(backing.add(other.backing, MathContext.DECIMAL128))
    operator fun minus(other: SciNumber) = SciNumber(backing - other.backing)
    operator fun times(other: SciNumber) = SciNumber(backing * other.backing)
    operator fun div(other: SciNumber) = SciNumber(backing.divide(other.backing, MathContext.DECIMAL128))

    operator fun unaryMinus() = SciNumber(-backing)
    operator fun compareTo(other: SciNumber) = backing.compareTo(other.backing)
    fun pow(n: Int) = SciNumber(backing.pow(n, MathContext.DECIMAL128)) // Context needed here?
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

    private fun doubleFunction(op : (Double) -> Double) = SciNumber(BigDecimal(op(backing.toDouble()), MathContext(backing.precision())))

    override fun equals(other: Any?): Boolean = other is SciNumber && this.backing == other.backing
    override fun hashCode(): Int = backing.hashCode()
    override fun toString(): String = backing.toPlainString()

    fun reciprocal() = SciNumber(BigDecimal.ONE.divide(backing, MathContext.DECIMAL128))
    fun toDouble(): Double = backing.toDouble()
}