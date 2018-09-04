package com.ethshea.digits.evaluator

import com.ethshea.digits.units.NaturalUnit
import com.ethshea.digits.SciNumber
import com.ethshea.digits.units.UnitSystem
import com.ethshea.digits.parser.DigitsLexer
import com.ethshea.digits.parser.DigitsParser
import com.ethshea.digits.parser.DigitsParserBaseVisitor
import com.ethshea.digits.units.isNumber
import com.ethshea.digits.units.parseNumber
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ConsoleErrorListener
import org.antlr.v4.runtime.misc.Interval

/**
 * @author Ethan
 */

data class ErrorMessage(val message: String, val position: Interval)

// TODO Why does this have to be generic?
data class ParseResult<T>(val value: T, val location: Interval? = null, val errors: Collection<ErrorMessage> = listOf()) {
    fun <R, A> invoke(argument: A, operation: (T, A) -> R) = ParseResult(operation(value, argument), location, errors)
    fun <R> invoke(operation: (T) -> R) = ParseResult(operation(value), location, errors)

    fun <R, A> combine(argument: ParseResult<A>, operation: (T, A) -> R) = ParseResult(operation(value, argument.value), location, errors + argument.errors)

    fun error(message: ErrorMessage) = ParseResult(value, location, errors + listOf(message))
}

fun evaluateExpression(input: String) : ParseResult<Quantity> {
    val lexer = DigitsLexer(CharStreams.fromString(input))
    lexer.removeErrorListener(ConsoleErrorListener.INSTANCE) // Error messages enabled by default -_-
    val tokens = CommonTokenStream(lexer)
    val parser = DigitsParser(tokens)
    parser.removeErrorListener(ConsoleErrorListener.INSTANCE)

    return parser.expression()?.accept(Evaluator) ?: ParseResult(Quantity.Zero)
}

// A null ParseResult means use whatever the identity element for the operation is
object Evaluator : DigitsParserBaseVisitor<ParseResult<Quantity>?>() {
    override fun visitLiteral(ctx: DigitsParser.LiteralContext): ParseResult<Quantity> {
        val value = SciNumber(ctx.value().text)

        return ParseResult(Quantity(value))
    }

    override fun visitAssignUnit(ctx: DigitsParser.AssignUnitContext): ParseResult<Quantity>? {
        val valueResult = ctx.expression().accept(this)
        val unitResult = parseUnit(ctx.unit().text, ctx.unit().sourceInterval)

        return if (valueResult == null) {
            null // Not sure if this is what I want to return here
        } else if (!valueResult.value.unit.dimensionallyEqual(NaturalUnit())) {
            valueResult.error(ErrorMessage("Two units", ctx.unit().sourceInterval))
        } else {
            valueResult.combine(unitResult) { value, unit -> Quantity(value.value, unit) }
        }
    }

    override fun visitParenthesizedExpression(ctx: DigitsParser.ParenthesizedExpressionContext): ParseResult<Quantity>? {
        return ctx.expression().accept(this)
    }

    override fun visitSumExpression(ctx: DigitsParser.SumExpressionContext): ParseResult<Quantity> {
        val lhsResult = ctx.lhs.accept(this) ?: ParseResult(Quantity.Zero)
        val rhsResult = ctx.rhs.accept(this) ?: ParseResult(Quantity.Zero)
        val operation = if (ctx.operation.type == DigitsLexer.PLUS) Quantity::plus else Quantity::minus

        return lhsResult.combine(rhsResult, operation)
    }

    override fun visitProductExpression(ctx: DigitsParser.ProductExpressionContext): ParseResult<Quantity> {
        val lhsResult = ctx.lhs.accept(this) ?: ParseResult(Quantity.One)
        val rhsResult = ctx.rhs.accept(this) ?: ParseResult(Quantity.One)
        val operation = if (ctx.operation.type == DigitsLexer.TIMES) Quantity::times else Quantity::div

        return lhsResult.combine(rhsResult, operation)
    }

    override fun visitExponent(ctx: DigitsParser.ExponentContext): ParseResult<Quantity> {
        val baseResult = ctx.base.accept(this) ?: ParseResult(Quantity.One)
        val exponent = parseNumber(ctx.exponent.text)

        return baseResult.invoke { base -> base.pow(exponent) }
    }

    override fun visitUnaryMinus(ctx: DigitsParser.UnaryMinusContext): ParseResult<Quantity>? {
        val argument = ctx.expression().accept(this)

        return argument?.invoke(Quantity::unaryMinus)
    }
}

fun parseUnit(input: String, location: Interval) : ParseResult<NaturalUnit> {
    var invert = false
    var unit = UnitSystem.void
    var lastUnit : NaturalUnit? = null
    val errors = mutableListOf<ErrorMessage>()
    val tokens = TokenIterator(input, location)

    val prefix = tokens.nextLargest(UnitSystem.prefixAbbreviations)
    if (prefix != null) {
        if (prefix.abbreviation == "m" && !(input.length > 1 && input[1].isLetter())) {// Special case for mili which conflicts with meters
            unit = UnitSystem.unitAbbreviations["m"]!!
            lastUnit = unit
        } else {
            unit = prefix
        }
    }

    while (tokens.hasNext()) {
        if (isNumber(tokens.peek())) {
            // Don't support multiple digit superscripts
            val exponent = parseNumber("" + tokens.next())
            if (lastUnit == null) {
                errors.add(ErrorMessage("Exponent before unit", tokens.lastTokenLocation))
            } else {
                unit += lastUnit * (exponent - 1) // -1 because we already added the unit in
            }
            continue
        }

        val newUnit = tokens.nextLargest(UnitSystem.unitAbbreviations)
        if (newUnit != null) {
            lastUnit = if (invert) -newUnit else newUnit
            unit += lastUnit
        } else if (tokens.isNext("/")) {
            invert = true
            tokens.consume("/")
        } else {
            tokens.next()
            errors.add(ErrorMessage("Unknown unit:", location)) // TODO fix position
        }
    }

    return ParseResult(unit, location, errors)
}