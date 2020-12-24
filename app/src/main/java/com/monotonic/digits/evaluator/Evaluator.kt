package com.monotonic.digits.evaluator

import com.monotonic.digits.parseNumber
import com.monotonic.digits.parser.DigitsLexer
import com.monotonic.digits.parser.DigitsParser
import com.monotonic.digits.parser.DigitsParserBaseVisitor
import com.monotonic.digits.units.PrefixUnit
import com.monotonic.digits.units.UnitSystem
import org.antlr.v4.runtime.misc.Interval
import java.math.BigDecimal
import kotlin.math.abs

/**
 * @author Ethan
 */

fun evaluateExpression(input: String) : ParseResult<Quantity> {
    return parse(input, Evaluator, Quantity.Zero)
}

// A null ParseResult means use whatever the identity element for the operation is
object Evaluator : DigitsParserBaseVisitor<ParseResult<Quantity>>() {
    private val constants = mapOf(
            "π" to Math.PI,
            "pi" to Math.PI,
            "Pi" to Math.PI,
            "PI" to Math.PI,
            "pI" to Math.PI,
            "e" to Math.E,
            "τ" to 2 * Math.PI,
            "φ" to 1.618033988749894848204586834
    )

    private val functions = mapOf(
            "sin" to wrapQuantityOperation(Quantity::sin),
            "cos" to wrapQuantityOperation(Quantity::cos),
            "tan" to wrapQuantityOperation(Quantity::tan),

            "sinh" to wrapQuantityOperation(Quantity::sinh),
            "cosh" to wrapQuantityOperation(Quantity::cosh),
            "tanh" to wrapQuantityOperation(Quantity::tanh),

            "asin" to wrapQuantityOperation(Quantity::asin),
            "acos" to wrapQuantityOperation(Quantity::acos),
            "atan" to wrapQuantityOperation(Quantity::atan),

            "sec" to wrapQuantityOperation(Quantity::sec),
            "csc" to wrapQuantityOperation(Quantity::csc),
            "cot" to wrapQuantityOperation(Quantity::cot),

            "√" to ::sqrt,
            "sqrt" to ::sqrt,

            "ln" to wrapQuantityOperation { q -> q.log(BigDecimal.valueOf(Math.E)) },
            "log₁₀" to wrapQuantityOperation { q -> q.log(BigDecimal.TEN) },
            "log" to wrapQuantityOperation { q -> q.log(BigDecimal.TEN) },
            "log₂" to wrapQuantityOperation { q -> q.log(BigDecimal("2")) },

            "exp" to wrapQuantityOperation(Quantity::exp)
    )

    private fun sqrt(quantity: Quantity, interval: Interval) =
        if (quantity.unit.isEven())
            ParseResult(quantity.sqrt(), interval)
        else
            ParseResult(quantity.sqrt(), interval, ErrorMessage("Unit is not even", interval))

    fun wrapQuantityOperation(operation: (Quantity) -> Quantity) : (Quantity, Interval) -> ParseResult<Quantity> =
            { quantity, interval ->  ParseResult(operation(quantity.normalized()), interval) }

    override fun visitUnaryMinus(ctx: DigitsParser.UnaryMinusContext): ParseResult<Quantity> {
        val argument = ctx.argument?.accept(this) ?: ParseResult(Quantity.Zero, ctx.sourceInterval, "Inferred lhs in unary -")

        return argument.invoke(Quantity::unaryMinus)
    }

    override fun visitSumExpression(ctx: DigitsParser.SumExpressionContext): ParseResult<Quantity> {
        val lhsResult = ctx.lhs?.accept(this) ?: ParseResult(Quantity.Zero, ctx.lhs.sourceInterval, ErrorMessage("Inferred lhs in +", intervalOf(ctx.operation)))
        val rhsResult = ctx.rhs?.accept(this) ?: ParseResult(Quantity.Zero, ctx.rhs.sourceInterval, ErrorMessage("Inferred rhs in +", intervalOf(ctx.operation)))
        val operation = if (ctx.operation.type == DigitsLexer.PLUS) Quantity::plus else Quantity::minus

        if (lhsResult.value.unit.dimensionallyEqual(rhsResult.value.unit)) {
            return lhsResult.combine(rhsResult, ctx.sourceInterval, operation)
        } else {
            val fixedRhs = rhsResult.invoke { rhs -> Quantity(rhs.value, lhsResult.value.unit) }
            return lhsResult.combine(fixedRhs, ctx.sourceInterval, operation).error(ErrorMessage("Incompatable units", rhsResult.location))
        }
    }

    override fun visitProductExpression(ctx: DigitsParser.ProductExpressionContext): ParseResult<Quantity> {
        val lhsResult = ctx.lhs?.accept(this) ?: ParseResult(Quantity.One, ctx.lhs.sourceInterval, ErrorMessage("Inferred lhs in *", intervalOf(ctx.operation)))
        val rhsResult = ctx.rhs?.accept(this) ?: ParseResult(Quantity.One, ctx.rhs.sourceInterval, ErrorMessage("Inferred rhs in *", intervalOf(ctx.operation)))
        val operation = if (ctx.operation.type == DigitsLexer.TIMES) Quantity::times else Quantity::div

        return lhsResult.combine(rhsResult, ctx.sourceInterval, operation)
    }

    override fun visitExponent(ctx: DigitsParser.ExponentContext): ParseResult<Quantity> {
        val baseResult = ctx.base?.accept(this) ?: ParseResult(Quantity.One, ctx.base.sourceInterval, ErrorMessage("Inferred base in ^", ctx.base.sourceInterval))
        val exponentMag = if (ctx.Digit().isEmpty()) 1 else parseNumber(ctx.Digit().joinToString("") { it.text })
        val sign = if (ctx.sign == null) 1 else -1

        // Will need to be changed for expressions in the exponent
        // Make sure to verify the base and exponent units
        val exponent = Quantity(SciNumber.Real(sign * exponentMag))

        return baseResult.invoke { base -> base.pow(exponent) }
    }

    // TODO try and factor this
    override fun visitValueExpression(ctx: DigitsParser.ValueExpressionContext): ParseResult<Quantity> {
        if (ctx.terms.isEmpty()) {
            return ParseResult(Quantity.Zero, ctx.sourceInterval, "Empty value") // Ctx  may just be the + character
        }

        var value = ParseResult(Quantity.One, ctx.sourceInterval)

        val doubleUnits = UnitSystem.prefixAbbreviations.keys.intersect(UnitSystem.unitAbbreviations.keys)
        val allAbbreviations = UnitSystem.unitAbbreviations.toMutableMap()
        for ((k, v) in UnitSystem.prefixAbbreviations) {
            allAbbreviations[k] = v
        }

        val unitQuantities = UnitSystem.unitAbbreviations.mapValues { Quantity(SciNumber.One, it.value)}
        val constantQuantities = constants.mapValues { Quantity(SciNumber.Real(it.value)) }
        val alphabeticQuantities = constantQuantities + unitQuantities

        var nextExponentBase : Quantity? = null
        var nextExponentPrefix = Quantity.One
        var functionName : String? = null
        var functionInterval : Interval? = null
        for (term in ctx.terms) {
            when (term) {
                is DigitsParser.AlphabeticContext -> {
                    // Split up the alphabetic parts ourselves
                    val tokens = TokenIterator(term.text, term.sourceInterval)

                    // Check for prefixes
                    if (functions.containsKey(tokens.peekRest())) {
                        functionName = tokens.rest()
                        functionInterval = tokens.previousLocation
                    } else {
                        val first = tokens.nextLargest(allAbbreviations)
                        if (first != null) {
                            if (doubleUnits.contains(first.abbreviation) && !tokens.hasNext()  // Special case for mili which conflicts with meters
                                    || first !is PrefixUnit) {
                                val unit = unitQuantities[first.abbreviation]!!
                                value = value.invoke { it * unit }
                                nextExponentBase = unit
                            } else { // Normal prefix
                                value = value.invoke { it * Quantity(SciNumber.One, first) }
                                nextExponentPrefix = Quantity(SciNumber.One, first)

                                if (!tokens.hasNext()) {
                                    value = value.error(ErrorMessage("Prefix without unit", tokens.previousLocation))
                                }
                            }
                        }
                    }

                    while(tokens.hasNext()) {
                        // Functions must be at the end of a sequence of letters because their arguments will be in the next section
                        val rest = tokens.peekRest()
                        if (functions.containsKey(rest)) {
                            functionName = tokens.rest()
                            functionInterval = tokens.previousLocation
                        } else {
                            val next = tokens.nextLargest(alphabeticQuantities)
                            if (next == null) {
                                // Not sure what value to use here
                                val consumedRest = tokens.rest()
                                value = value.error(ErrorMessage("$consumedRest is not a known constant", ctx.sourceInterval))
                            } else {
                                value = value.invoke(next) { v, n -> v * n }
                                nextExponentBase = next
                            }
                        }
                    }
                }
                is DigitsParser.TermExponentContext -> {
                    val exponent = parseNumber(term.text)

                    if (nextExponentBase == null) {
                        value = value.error(ErrorMessage("Exponent with no base", term.sourceInterval))
                    } else if (abs(exponent) >= 100){
                        value = value.error(ErrorMessage("Exponent too large", term.sourceInterval))
                    } else {
                        val prevTerm = nextExponentBase // Scoot around kotlin's type system
                        val exponentQuantity = Quantity(SciNumber.Real(exponent - 1)) // -1 since we've already incorporated it once
                        value = value.invoke { it * prevTerm.pow(exponentQuantity) * nextExponentPrefix.pow(exponentQuantity) }
                    }
                }
                is DigitsParser.NumericLiteralContext -> {
                    if (nextExponentBase == null) {
                        val literal = Quantity(SciNumber.Real(term.text))
                        value = value.invoke { v -> v * literal }
                        nextExponentBase = literal
                    } else {
                        if (term.text.contains(".")) {
                            value = value.error(ErrorMessage("Non-integral exponents are not supported", term.sourceInterval))
                        } else {
                            val exponentMag = parseNumber(term.text)

                            if (abs(exponentMag) >= 100) {
                                value = value.error(ErrorMessage("Exponent too large", term.sourceInterval))
                            } else {
                                val prevTerm = nextExponentBase // Scoot around kotlin's type system
                                val exponentQuantity = Quantity(SciNumber.Real(exponentMag - 1)) // -1 since we've already incorporated it once
                                value = value.invoke { it * prevTerm.pow(exponentQuantity) * nextExponentPrefix.pow(exponentQuantity) }
                            }

                            nextExponentBase = null
                        }
                    }
                }
                is DigitsParser.ParenthesizedExpressionContext -> {
                    val innerValue = term.inner?.accept(this)
                    if (innerValue == null) {
                        value = value.error(ErrorMessage("Empty parens", term.sourceInterval))
                    } else {
                        if (functionName == null) {
                            value = value.combine(innerValue, ctx.sourceInterval) { v, t -> v * t }
                            nextExponentBase = innerValue.value
                        } else {
                            val function = functions[functionName]

                            if (function == null) {
                                value = value.combine(innerValue, ctx.sourceInterval) { v, t -> v * t }
                                        .error(ErrorMessage("Unknown function/constant", functionInterval!!))
                                nextExponentBase = innerValue.value
                            } else {
                                val result = function(innerValue.value, term.inner.sourceInterval)
                                value = value.combine(result, ctx.sourceInterval) { v, t -> v * t }
                                nextExponentBase = result.value
                            }
                        }
                    }
                }
                is DigitsParser.ScientificNotationContext -> {
                    if (nextExponentBase == null) {
                        value = value.error(ErrorMessage("Missing mantissa", term.sourceInterval))
                    } else {
                        val sign = if (term.sign == null) 1 else -1
                        val exponent = if (term.Digit().isEmpty()) 0 else parseNumber(term.Digit().joinToString("") { it.text }) * sign
                        value = value.invoke { it * Quantity(SciNumber.Real(10).pow(SciNumber.Real(exponent))) }
                    }
                }
            }
        }

        return value
    }
}
