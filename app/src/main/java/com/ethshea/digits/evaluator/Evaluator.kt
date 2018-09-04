package com.ethshea.digits.evaluator

import com.ethshea.digits.units.NaturalUnit
import com.ethshea.digits.SciNumber
import com.ethshea.digits.units.UnitSystem
import com.ethshea.digits.parser.DigitsLexer
import com.ethshea.digits.parser.DigitsParser
import com.ethshea.digits.parser.DigitsParserBaseVisitor
import com.ethshea.digits.units.unsuperscript
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
        val unitResult = if (ctx.unit() == null)
            ParseResult(NaturalUnit()) // Using parseresult is strange here
            else parseUnit(ctx.unit().text, ctx.unit().sourceInterval)

        return unitResult.invoke { unit -> Quantity(value, unit) }
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

    override fun visitExponent(ctx: DigitsParser.ExponentContext): ParseResult<Quantity>? {
        val baseResult = ctx.base.accept(this) ?: ParseResult(Quantity.One)
        val exponent = Integer.parseInt(unsuperscript(ctx.exponent.text))

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
    val errors = mutableListOf<ErrorMessage>()
    val tokens = TokenIterator(input)

    val prefix = tokens.nextLargest(UnitSystem.prefixAbbreviations)
    if (prefix != null) {
        if (prefix.abbreviation == "m" && input.split("/")[0].length == 1) { // Special case for mili which conflicts with meters
            unit += UnitSystem.unitAbbreviations["m"]!!
        } else {
            unit += prefix
        }
    }

    while (tokens.hasNext()) {
        val newUnit = tokens.nextLargest(UnitSystem.unitAbbreviations)
        if (newUnit != null) {
            unit += if (invert) -newUnit else newUnit
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