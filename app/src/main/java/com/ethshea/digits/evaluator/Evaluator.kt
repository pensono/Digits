package com.ethshea.digits.evaluator

import com.ethshea.digits.NaturalUnit
import com.ethshea.digits.UnitSystem
import com.ethshea.digits.parser.DigitsLexer
import com.ethshea.digits.parser.DigitsParser
import com.ethshea.digits.parser.DigitsParserBaseVisitor
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ConsoleErrorListener
import java.math.BigDecimal

/**
 * @author Ethan
 */

data class ErrorMessage(val message: String, val position: Int)

// TODO Why does this have to be generic?
data class ParseResult<T>(val value: T, val errors: Collection<ErrorMessage> = listOf()) {
    fun <R, A> invoke(argument: A, operation: (T, A) -> R) = ParseResult(operation(value, argument), errors)
    fun <R> invoke(operation: (T) -> R) = ParseResult(operation(value), errors)

    fun <R, A> combine(argument: ParseResult<A>, operation: (T, A) -> R) = ParseResult(operation(value, argument.value), errors + argument.errors)
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
        val value = BigDecimal(ctx.value().text)
        val unitResult = parseUnit(TokenIterator(ctx.unit().text))

        return unitResult.invoke { unit -> Quantity(value, unit)}
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

    override fun visitUnaryMinus(ctx: DigitsParser.UnaryMinusContext): ParseResult<Quantity>? {
        val argument = ctx.expression().accept(this)

        return argument?.invoke(Quantity::unaryMinus)
    }
}

fun parseUnit(tokens: TokenIterator) : ParseResult<NaturalUnit> {
    var invert = false
    var unit = UnitSystem.void
    val errors = mutableListOf<ErrorMessage>()

    while (tokens.hasNext()) {
        val abbreviation = tokens.nextLargest(UnitSystem.abbreviations)
        if (abbreviation != null) {
            val newUnit = UnitSystem.byAbbreviation(abbreviation)!!
            unit += if (invert) -newUnit else newUnit
            tokens.consume(abbreviation)
        } else if (tokens.isNext("/") && tokens.nextLargest(UnitSystem.abbreviations.map { a -> "/" + a }) != null) { // Lame fix for division at the end
            invert = true
            tokens.consume("/")
        } else {
            tokens.next()
            errors.add(ErrorMessage("Unknown unit:", 0)) // TODO fix position
        }
    }

    return ParseResult(unit, errors)
}